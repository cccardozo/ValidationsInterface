import os
import sys
import time
import RealtimeAppBuilder

#==============================================================================#
# Realtime Framework Builder Configuration
#==============================================================================#

config = {}

#------------------------------------------------------------------------------#
# Project   
#------------------------------------------------------------------------------#
config["Project"] = \
{
      "Name"                      : "GenericInterfaceTranTest",
      "Version"                   : "v1.1.2-r5.6",
      "BuildNumber"               : time.strftime("%y%m%d", time.localtime(time.time()))+"1",
}

#------------------------------------------------------------------------------#
# BuildEnvironment   
#------------------------------------------------------------------------------#
config["BuildEnvironment"] = \
   {
      "JDKHome"                   : "C:\\Program Files\\Postilion\\realtime\\jdk",
      "OutputDir"                 : ".\\build"
   }


#------------------------------------------------------------------------------#
# Tasks   
#------------------------------------------------------------------------------#
config["Tasks"] = \
{
      "GenericInterfaceTranTest"    :
         {
            "TaskType"            : RealtimeAppBuilder.TASK_TYPE_INTERCHANGE,
            "Service"             : True,
            "Description"         : "Interface GenericTanTest.",
            "MainClass"           : "postilion.realtime.sdk.env.App",
            "ClassArguments"      : 
               [
                  "GenericInterfaceTranTest",
                  0,
                  "postilion.realtime.sdk.node.InterchangeProcessor",
                  "postilion.realtime.sdk.node.Interchange",
                  "postilion.realtime.generictrantest.GenericInterfaceTranTest"
               ]
         }
}

#------------------------------------------------------------------------------#
# Events
#------------------------------------------------------------------------------#
config["Events"] = \
   {
      "EventResourceFile"    : ".\\resources\\events\\events.er",
   }


#------------------------------------------------------------------------------#
# Java    
#------------------------------------------------------------------------------#
config["Java"] = \
   {
      "BasePackage"          : "postilion.realtime.fware",
	  "ClassPaths"                : \
         [
            ".\\resources\\jars\\lombok.jar",
            ".\\resources\\jars\\json-20180130.jar"             
            
         ],
      "SourceDirs"           : \
         [
            (".\\source\\java", RealtimeAppBuilder.INCLUDE_RECURSE)
         ],
    }
	   
  

#------------------------------------------------------------------------------#
# Documentation																					 #
#------------------------------------------------------------------------------#
# config["Documentation"] = \
	# {
		 # "userguide" 			: 
            # {                 	
				 # "Name"			: "User Guide",
				 # "Type"			: "CHM",
				 # "SourceDir"	: "doc\\userguide",
				 # "Project"		: "ug_GenericInterfaceTranTest",
				 # "Replacements"	        :   
					 # {           	
						 # "Files"	: ["Title.htm"]
					 # }
			 # },
		 # "releasenotes" 		: 
			 # {                 	
				 # "Name"			: "Release Notes",
				 # "Type"			: "CHM",
				 # "SourceDir"	: "doc\\releasenotes",
				 # "Project"		: "rn_GenericInterfaceTranTest",
			 # }
	# }

#------------------------------------------------------------------------------#
# Database																	   #
#------------------------------------------------------------------------------#

#config["Database"] = \
#   [
#      (
#         "realtime",
#         {
#			"SourceDirs"	      : \
#               [
#                  (".\\source\\sql\\realtime", RealtimeAppBuilder.INCLUDE_NO_RECURSE),
#				  (".\\build\\sql\\support_events",RealtimeAppBuilder.INCLUDE_NO_RECURSE),
#				],					  
#         }
#      )
#   ]
#   
#------------------------------------------------------------------------------#
# Release																	   #
#------------------------------------------------------------------------------#

config["Release"] = \
	{
		"Packaging"				: \
			[	
				(RealtimeAppBuilder.WINDOWS_ONLY,"build\\install\\standard_edition\\setup.exe", "setup.exe"),
			]
	}
	
#==============================================================================#
# Realtime Framework Builder										          #
#==============================================================================#

# Get the target and any target arguments that are present
target = RealtimeAppBuilder.getTargetName(sys.argv)
target_args = RealtimeAppBuilder.getTargetArguments(sys.argv)
   
# Build the target project.
RealtimeAppBuilder.RealtimeAppBuilder(config).buildProject(build_target=target, build_target_args=target_args)
