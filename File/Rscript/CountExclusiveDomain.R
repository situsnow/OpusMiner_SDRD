library(dplyr)
# field12 == 1 means "<=50K"
# field12 == 2 means ">50K"
dim(filter(Adult_R,
           field9 == "Male"
           ))[1]

dim(filter(Adult_R,
           field7 != "Not-in-family",
           field3 != "HS-grad",
           field4 != "EduNum9",
           field7 != "Unmarried",
           field1 != "Age17-23",
           field7 != "Own-child",
           (field5 != "Never-married" | field1 != "Age17-23"),
           (field7 != "Not-in-family" | field2 != "Private"),
           (field2 != "Private" | field3 != "HS-grad"),
           (field2 != "Private" | field4 != "EduNum9"),
           field3 != "Some-college",
           field4 != "EduNum10",
           (field5 != "Never-married" | field7 != "Own-child"),
           (field2 != "Private" | field1 != "Age17-23"),
           field6 != "Adm-clerical",
           (field2 != "Private" | field7 != "Own-child"),
           field6 != "Other-service",
           field1 != "Age24-31",
           (field5 != "Never-married" | field2 != "Private" | field1 != "Age17-23"),
           (field5 != "Never-married" | field2 != "Private" | field7 != "Own-child"),
           (field5 != "Never-married" | field7 != "Not-in-family"),
           (field1 != "Age17-23" | field7 != "Own-child"),
           (field5 != "Never-married" | field3 != "Some-college"),
           (field5 != "Never-married" | field4 != "EduNum10"),
           field10 != "Hour21-35",
           (field5 != "Never-married" | field1 != "Age17-23" | field7 != "Own-child"),
           field12 == "1",
           field9 == "Female"
           ))[1]

