#!/bin/sh

groovy -cp dist/h2datastore.jar:dist/lib/json.jar:dist/lib/h2-1.1.117.jar:dist/lib/commons-lang-2.4.jar:dist/lib/log4j-1.2.15.jar $1
