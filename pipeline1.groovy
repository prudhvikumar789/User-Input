def buildnumber = env.BUILD_NUMBER
def buildstatus="Success";
def workspace = env.WORKSPACE
// def myjavapath ="${params.JavaPath}"
def prefpath = "/tmp/3.6.3/Appzillon/Bin/Preferences"
def jarpath = "appzillonstudio-commandbuild-ci.jar"

pipeline {
  agent none
  stages {
    stage('Build') {
      agent { label 'mysystem' }
      steps {
        script {
            sh "echo Helloworld"
        }
      }
    }
    stage('Decide Java and mvn version') {
      agent none
      steps {
        script {
          env.JAVA_AND_MAVEN_PATH = input message: 'User input required',
              parameters: [choice(name: 'java and mvn version', choices: 'no\nyes', description: 'Choose "yes" if you want to use DEFAULT path')]
        }
      }
    }
    stage('Java and mvn version') {
      agent { label 'mysystem' }
      when {
        environment name: 'JAVA_AND_MAVEN_PATH', value: 'yes'
      }
      steps {
        script {
                def val1 = "JAVAPATH";
                def val2 = "$JAVA_HOME";
                def path1 = "/tmp/3.6.3/Appzillon/Bin/Preferences/Environment.json";
                sh "sed -i 's+$val1+$val2+g' $path1"
                def val3 = "MAVENPATH";
                def val4 = "$M2_HOME";
                sh "sed -i 's+$val3+$val4+g' $path1"
                sh "cd /tmp/3.6.3/Appzillon/lib && java -jar $jarpath $prefpath"
                def val5 = "JAVAPATH";
                def val6 = "$JAVA_HOME";
                def path2 = "/tmp/3.6.3/Appzillon/Bin/Preferences/Environment.json";
                sh "sed -i 's+$val6+$val5+g' $path2"
                def val7 = "MAVENPATH";
                def val8 = "$M2_HOME";
                sh "sed -i 's+$val8+$val7+g' $path2"
        }
      }

    }
    stage('Manual Java and mvn version') {
      agent { label 'mysystem' }
      when {
        environment name: 'JAVA_AND_MAVEN_PATH', value: 'no'
      }
      steps {
        build(job: "docker", propagate: false, wait: true)
      }

    }

  }
}
