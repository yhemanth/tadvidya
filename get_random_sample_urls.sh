#!/bin/sh
grep "OPTION VALUE=\"c" song_list.html  | gshuf | head -800 | cut -d'=' -f 2 | cut -d'>' -f 1 | tr -d '"' | awk '{print "https://www.karnatik.com/"$1}'
