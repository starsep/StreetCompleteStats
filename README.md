[StreetComplete](https://github.com/streetcomplete/StreetComplete) Stats.
Hosted at https://starsep.com/StreetCompleteStats

Inspired by https://streetcompleteness.haukauntrie.de

# Docker
```
docker build -t streetcompletestats .
docker run --rm \
    -v "$(pwd)/data:/app/data" \
    --env GITHUB_USERNAME=example \
    --env GITHUB_TOKEN=12345 \
    --env TZ=Europe/Warsaw \
    -t streetcompletestats
```

