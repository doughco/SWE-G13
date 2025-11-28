import pyudev
import subprocess
import datetime
import time
import json
import threading
import os

CAPTURE_DIR = "/home/aaaaa/captures"
LAST_CAPTURE_FILE = "/home/aaaaa/last_capture.json"
PHOTO_INTERVAL = 30  # seconds between each photo taken to prevent the detection system from taking many photos, as it detects multiple times instead of once during a connection.
SENDER_INTERVAL = 30  

# the capture folder exists
os.makedirs(CAPTURE_DIR, exist_ok=True)

last_capture_time = 0
last_sender_time = 0


def capture_photo():
    global last_capture_time
    now = time.time()
    if now - last_capture_time < PHOTO_INTERVAL:
        print(f"Interval not elapsed photo not taken.")
        return None

    timestamp = datetime.datetime.now().strftime("%Y-%m-%d_%H-%M-%S")
    filename = f"photo_{timestamp}.jpg"
    filepath = os.path.join(CAPTURE_DIR, filename)

    cmd = [
        "ffmpeg",
        "-f", "v4l2",
        "-video_size", "1280x720",
        "-i", "/dev/video0",
        "-frames:v", "1",
        "-y",  # overwrite if exists
        filepath
    ]
    try:
        subprocess.run(cmd, check=True, stdout=subprocess.DEVNULL, stderr=subprocess.DEVNULL)
        print(f"[Photo captured → {filepath}]")
        last_capture_time = now
        return filepath

    except subprocess.CalledProcessError:
        print("[ERROR] Failed to capture photo.")
        return None


def save_last_capture(filepath):
    if filepath is None:
        return

    data = {
        "photo": filepath,
        "timestamp": datetime.datetime.now().isoformat()
    }
    with open(LAST_CAPTURE_FILE, "w") as f:
        json.dump(data, f)


def launch_sender():
    global last_sender_time
    now = time.time()
    if now - last_sender_time < SENDER_INTERVAL:
        print(" Interval not elapsed, sender.py not launched.")
        return
    threading.Thread(target=lambda: subprocess.run(["python3", "/home/aaaaa/sender.py"])).start()
    last_sender_time = now
    print("sender.py launched")


def usb_event(device):
    print(f"USB detected: {device.action}, device: {device.device_node}")
    filepath = capture_photo()
    save_last_capture(filepath)
    launch_sender()


def monitor_usb():
    context = pyudev.Context()
    monitor = pyudev.Monitor.from_netlink(context)
    monitor.filter_by(subsystem='usb')
    observer = pyudev.MonitorObserver(monitor, callback=usb_event, name='usb-monitor-thread')
    observer.start()
    print(" started waiting for a USB device ")
    try:
        while True:
            time.sleep(1)
    except KeyboardInterrupt:
        observer.stop()
        print("Script stopped.")


if __name__ == "__main__":
    monitor_usb()