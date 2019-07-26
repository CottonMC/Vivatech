import json
import os
import re
from string import Template

resources = json.load(open("resources.json", "r"))

if not os.path.exists("out/"):
    os.makedirs("out/")

print("Generating standard resources recipes")
for tierName, tier in resources.items():
    print("+ Generating {} tier resources recipes".format(tierName))

    for resourceTypeName, resourceType in tier.items():
        print("| + Generating {} type resources recipes".format(resourceTypeName))

        recipes = os.listdir(resourceTypeName)

        for resource in resourceType:
            print("| | + Generating {} recipes".format(resource))

            for recipeName in recipes:
                with open("{}/{}".format(resourceTypeName, recipeName), "r") as recipeFile:
                    recipe = recipeFile.read()
                    recipeFile.close()

                recipe = Template(recipe).substitute(
                    resource = resource, 
                    tier = tierName
                )

                recipeName = recipeName.format(resource)
                with open("out/{}".format(recipeName), "w+") as recipeFile:
                    recipeFile.write(recipe)
                    recipeFile.close()
                print("| | | Generated {} recipe".format(recipeName))
