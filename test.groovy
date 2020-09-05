job('GitHub-Code') {
	triggers {
        upstream('GroovyScript', 'SUCCESS')
    }
    scm {
        github('ShirshaDatta/Automating-the-deployment-of-a-containerized-app-in-K8S-using-Groovy', 'master')
    }
    steps {
       shell(''' sudo cp * -v /Automating-the-deployment-of-a-containerized-app-in-K8S-using-Groovy
       sudo docker build -t shirsha30/httpd_server:latest /Automating-the-deployment-of-a-containerized-app-in-K8S-using-Groovy
       sudo docker push shirsha30/httpd_server:latest
       ''')
    }
}
job('Deployment') {
	triggers {
        upstream('GitHub-Code', 'SUCCESS')
    }
    
    steps {
       shell(''' fullfilename="/Automating-the-deployment-of-a-containerized-app-in-K8S-using-Groovy/*.html"
			filename=$(basename "$fullfilename")
			ext="${filename##*.}"
			echo $ext
           
			if [ $ext == html ];
			then
			   if  sudo  kubectl get deployment | grep myweb-deploy 
			   then
			      echo "Already Running"
				else
				  sudo kubectl create -k /Automating-the-deployment-of-a-containerized-app-in-K8S-using-Groovy/
				 fi
				 
			elif [ $ext == php ];
			then
			   if  sudo  kubectl get deployment | grep webserver-php 
			   then
				  echo "Already Running"
			      
				else
				sudo kubectl create -k /Automating-the-deployment-of-a-containerized-app-in-K8S-using-Groovy/
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
	   status=$(curl -o /dev/null -s -w "%{http_code}" http://192.168.99.100:32123/index.html)
		if [ $status == 200 ]
		then
		echo "running"
		else
		echo "failure"
		fi 
          
          ''')
    }
}



buildPipelineView('Task6') {
    title('Devops6')
    displayedBuilds(5)
    selectedJob('GitHub-Code')
    showPipelineParameters(true)
    refreshFrequency(20)
}