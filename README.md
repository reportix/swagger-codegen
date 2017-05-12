# Source

This repository contains code based on 28msec/swagger-codegen and swagger-api/swagger-codegen, both licensed under Apache 2.0.

# Extra features

- Java environment option `-DnoInlineModels`. This can be used to disable the generation of body parameter models. Useful if the generated models are wrong.
- No accept header are sent in the requests
- No empty client classes are generated
- It is possible to specify a list of values for query parameters that have an array type.
- Improved parameter and body serialization
- Exception contain the full request and response to help generate useful debug information. Method `ToStringDebug()` of APIException can be used for this purpose.  
- Extra properties:
	- `x-exclude` property can be specified on operations or parameters to completely exclude them from the clients.
	- `x-name` parameter property can be specified to override the name of a parameter. Useful especially for pattern parameters.
	- `x-description` parameter property can be specified to override the description of a parameter. Useful especially for pattern parameters.
	- `x-hardcoded-value` parameter property can be specified to exclude a parameter from a client method and always send an specified value for that parameter. 
	- `x-pattern` parameter property can be used to specify a family of parameters. Its value should be a regular expression that is used to restrict the parameter name. `x-name` should be used in conjunction to define the name of the method in the client. 

# Guide to merging upstream

## Creating the merge PR

In an empty folder:

1. Clone the upstream repository locally: `git clone git@github.com:swagger-api/swagger-codegen.git` 

2. Add the reportix remote: `git remote add reportix git@github.com:reportix/swagger-codegen.git`

3. Fetch the reportix remote: `git fetch reportix`

4. Push the master to the reportix master: `git push reportix master`   

In a cloned reportix swagger-codegen:

1. Pull the new reportix-master: `git checkout reportix-master` and `git pull origin reportix-master`

2. Pull the new master: `git checkout master` and `git pull origin master`

3. Create a merge branch: ``git checkout -b merge-upstream-`date +%Y-%m-%d-%H-%M-%S``` and push it `git push origin merge-upstream...`

4. Merge the reportix-master branch: `git merge reportix-master`

5. Create a PR to merge the new branch in reportix-master on Github

6. Analyze all changes as described below and make the necessary extra changes.
 
7. Merge the PR. 

## Analyze changes

In general all Reportix changes are marked by comments that delimit them.

### Analyzing Java changes

Carefully analyze the diff on the following classes:

- CodegenParameter
- CodegenOperator
- DefaultGenerator

And the following methods:
- fromParameter
- fromOperation
- postProcessOperations

### Analyze C# template changes

Difference the two folders `meld modules/swagger-codegen/src/main/resources/reportix-csharp/ modules/swagger-codegen/src/main/resources/csharp`