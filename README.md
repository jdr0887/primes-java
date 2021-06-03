# primes-java

## How to Build & Run
```
primes-java$ mvn clean install
primes-java$ mvn -q exec:exec -Dexec.executable="java" -Dexec.args="-cp %classpath com.kiluet.primes.SieveOfAtkinSerial 1000000000"
[2021-06-03 11:13:51] [INFO   ] ceiling: 1000000000 
[2021-06-03 11:14:10] [INFO   ] Duration: 00:00:19.642 
primes-java$ mvn -q exec:exec -Dexec.executable="java" -Dexec.args="-cp %classpath com.kiluet.primes.SieveOfEratosthenesSerial 1000000000"
[2021-06-03 11:20:46] [INFO   ] ceiling: 1000000000 
[2021-06-03 11:20:57] [INFO   ] Duration: 00:00:10.424 
primes-java$ mvn -q exec:exec -Dexec.executable="java" -Dexec.args="-cp %classpath com.kiluet.primes.SieveOfSundaramSerial 1000000000"
[2021-06-03 11:21:08] [INFO   ] ceiling: 1000000000 
[2021-06-03 11:21:38] [INFO   ] Duration: 00:00:29.589 
```