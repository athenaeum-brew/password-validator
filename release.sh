git tag -d password-validator-1.0.0
mvn release:prepare -DreleaseVersion=1.0.0 -DdevelopmentVersion=2.0.0-SNAPSHOT -Dtag1.0.0 -DautoVersionSubmodules=true
