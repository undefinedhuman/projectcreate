ssh undefinedhuman@playprojectcreate.com "cd /home/projectcreate/instances/dev/ && ./stop.sh && rm server.jar"
./gradlew :server:dist
scp ./server/build/libs/server.jar undefinedhuman@playprojectcreate.com:/home/projectcreate/instances/dev/server.jar
ssh undefinedhuman@playprojectcreate.com "cd /home/projectcreate/instances/dev/ && ./start.sh"
