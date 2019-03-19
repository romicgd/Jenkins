def isFolder(abstractJob){
 return abstractJob.getClass().getName() == "com.cloudbees.hudson.plugins.folder.Folder"
}

def collectJobRecursively(abstractJob,resultCollector){
  if(isFolder(abstractJob))
        {
            abstractJob.items.findAll().each{ nestedJob ->
            collectJobRecursively(nestedJob,resultCollector)
        }
  }
  else
  {
    //only add actual jobs to the list
    resultCollector << abstractJob
  }
}

def findAll(pattern = '.*'){
 def resultCollector = []
 Jenkins.instance.items.findAll().each{ job ->
    collectJobRecursively(job,resultCollector)
  }

  return resultCollector
  .findAll{
    // apply the regular expression here.
    // we are matching the Full Name (including folder names)
      (it.fullName =~ /$pattern/)
   }
}

findAll(".*")
.each{
  job ->
   if(job.lastBuild){
      println job.fullName + ':' + job.lastBuild.result
    }
  else {
    println job.fullName + " has not run yet";
  }
 }
