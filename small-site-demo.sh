#!/bin/bash
java -jar ./target/small-site-demo-0.0.4.jar \
-Dfile.encoding=UTF-8 \
--server.port=8080 \
--spring.profiles.active=dev \
--spring.mail.host=localhost \
--spring.mail.port=537 \
--spring.mail.username=mail \
--spring.mail.password=secret