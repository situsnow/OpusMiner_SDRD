library(dplyr)
# field12 == 1 means "<=50K"
# field12 == 2 means ">50K"
dim(filter(Adult_R,
           #field4 == "EduNum7",
           field10 == "Hour1-20"
           #field12 == 1
           ))[1]

