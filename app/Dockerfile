FROM gradle:7.4.2-jdk17

WORKDIR /app

COPY src .

RUN gradle installDist

CMD build/install/app/bin/app