#!/bin/sh

VERSION=1.0-SNAPSHOT

init_data() {
    docker run tadvidya-init:${VERSION} python3 parse_song_details.py /tv-init/data 192.168.1.8:9000    
}

init_data