#!/bin/bash
FILE=./PebbleConfigCreator.jar

if [ -f "$FILE" ]; then
  echo "starting..."
  java -jar PebbleConfigCreator.jar
else
  echo "[ERROR] PebbleConfigCreator.jar not found"
fi
