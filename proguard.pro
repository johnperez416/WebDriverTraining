-injars       target/webdrivertraining.1.0-SNAPSHOT.jar
-outjars      target/processed-webdrivertraining.1.0-SNAPSHOT.jar
-libraryjars  <java.home>/lib/rt.jar
-printmapping myapplication.map

-keep public class com.octopus.Main {
      public static void main(java.lang.String[]);
}

-keep public class com.octopus.LambdaEntry {
      public java.lang.String runCucumber(com.octopus.LambdaInput, com.amazonaws.services.lambda.runtime.Context);
}