#!/usr/bin/env bash
date=$(/bin/date '+%Y%m%d')
git pull
cd output
git pull
cd ..
./gradlew run
cd output
git config user.name "StreetCompleteStats Bot"
git config user.email "<>"
git add *
git commit -m "Update $date"
git push origin main
cd ..
