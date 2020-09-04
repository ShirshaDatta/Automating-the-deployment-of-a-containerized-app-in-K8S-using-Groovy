job('GitHub-Code') {
	triggers {
        upstream('GroovyScript', 'SUCCESS')
    }
    scm {
        github('ShirshaDatta/Automating-the-deployment-of-a-containerized-app-in-K8S-using-Groovy', 'master')
    }
    steps {
       shell(''' sudo cp * -v /Automating-the-deployment-of-a-containerized-app-in-K8S-using-Groovy.git
       sudo docker build -t shirsha30/httpd_server:v1 /Automating-the-deployment-of-a-containerized-app-in-K8S-using-Groovy.git
       sudo docker push shirsha30/httpd_server:v1 
       ''')
    }
}
job('Deployment') {
	triggers {
        upstream('GitHub-Code', 'SUCCESS')
    }
    
    steps {
       shell(''' fullfilename="/Automating-the-deployment-of-a-containerized-app-in-K8S-using-Groovy.git*.html"
			filename=$(basename "$fullfilename")
			ext="${filename##*.}"
			echo $ext
           
			if [ $ext == html ];
			then
			   if  sudo  kubectl get deployment | grep myweb-deploy 
			   then
			      echo "Already Running"
				else
				  sudo kubectl create -k /Automating-the-deployment-of-a-containerized-app-in-K8S-using-Groovy.git/
				 fi
				 
			elif [ $ext == php ];
			then
			   if  sudo  kubectl get deployment | grep webserver-php 
			   then
				  echo "Already Running"
			      
				else
				sudo kubectl create -k /Automating-the-deployment-of-a-containerized-app-in-K8S-using-Groovy.git/
			  fi	 
			else
			   echo "everything is working"
			fi	 
				 
			''')
    }
}

job('Testing') {
	triggers {
        upstream('Deployment', 'SUCCESS')
    }
    
    steps {
       shell('''
	   status=$(curl -o /dev/null -s -w "%{http_code}" http://192.168.99.100:32750)
		if [ $status == 200 ]
		then
		echo "running"
		//sudo python3 /task6/successmail.py
		else
		echo "failure"
		//sudo python3 /task6/failure.py
		fi 
          
          ''')
    }
}



buildPipelineView('Devops-Task6') {
    filterBuildQueue(true)
    filterExecutors(false)
    title('Devops-Task6')
    displayedBuilds(1)
    selectedJob('GroovyScript')
    alwaysAllowManualTrigger(false)
  showPipelineParameters(true)
  refreshFrequency(20)
}