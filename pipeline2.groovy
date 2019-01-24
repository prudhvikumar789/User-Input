def buildnumber = env.BUILD_NUMBER
def buildstatus="Success";
def workspace = env.WORKSPACE
// def myjavapath ="${params.JavaPath}"
def prefpath = "/tmp/3.6.3/Appzillon/Bin/Preferences"
def jarpath = "appzillonstudio-commandbuild-ci.jar"

pipeline {
    agent { label 'mysystem' }
    stages {
        stage('Example') {
            input {
                message "Should we continue?"
                ok "GO.!"
                submitter "alice,bob"
                parameters {
                    string(name: 'javaman', defaultValue: 'Enter the Path', description: 'GIVE ME THE PATH:')
                    string(name: 'mavman', defaultValue: 'Enter the Path', description: 'GIVE ME THE PATH:')
                }
            }
            steps {
                echo "JAVAPATH, ${javaman}"
                echo "MAVENPATH, ${mavman}"
                script {
                    def val1 = "JAVAPATH";
                    def val2 = "$javaman";
                    def path1 = "/tmp/3.6.3/Appzillon/Bin/Preferences/Environment.json";
                    sh "sed -i 's+$val1+$val2+g' $path1"
                    def val3 = "MAVENPATH";
                    def val4 = "$mavman";
                    sh "sed -i 's+$val3+$val4+g' $path1"
                    sh "cd /tmp/3.6.3/Appzillon/lib && java -jar $jarpath $prefpath"
                    def val5 = "JAVAPATH";
                    def val6 = "$javaman";
                    def path2 = "/tmp/3.6.3/Appzillon/Bin/Preferences/Environment.json";
                    sh "sed -i 's+$val6+$val5+g' $path2"
                    def val7 = "MAVENPATH";
                    def val8 = "$mavman";
                    sh "sed -i 's+$val8+$val7+g' $path2"
                }
            }
        }
    }
}
