#!/usr/bin/env bash
date=$(/bin/date '+%Y%m%d')
git clone https://$GITHUB_USERNAME:$GITHUB_TOKEN@github.com/starsep/StreetCompleteStats/ --depth 1 output --branch gh-pages
java -jar StreetCompleteStats-all.jar
cd output || exit 1
git config user.name "StreetCompleteStats Bot"
git config user.email "<>"
git add *
git commit -m "Update $date"
git push origin gh-pages
