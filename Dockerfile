FROM adoptopenjdk/maven-openjdk11:latest

RUN apt-get update && apt-get install -y software-properties-common
RUN apt-get update
RUN add-apt-repository ppa:mozillateam/ppa
RUN apt-get update
RUN apt-get -yq clean
RUN apt-get --yes --force-yes install firefox-esr

RUN groupadd -g 985 rhsvc && \
    useradd -m -r -u 985 -g rhsvc rhsvc

ENV M2_HOME=/home/rhsvc/.m2
RUN mkdir -p /home/rhsvc/.m2/repository
RUN ln -s /usr/bin/firefox-esr /usr/bin/firefox
COPY . /home/rhsvc/rh-cucumber
RUN chown -R rhsvc:rhsvc /home/rhsvc/rh-cucumber
COPY .maven.settings.xml /home/rhsvc/.m2/settings.xml
WORKDIR /home/rhsvc/rh-cucumber
USER rhsvc

CMD [ "mvn", "verify", "-Dmaven.repo.local=m2/repository"]
