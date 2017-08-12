
dim(filter(Adult_NoMacro, `EduNumEducation-Num` == "EduNum14",
           Occupation == "Exec-managerial",
           `Hours-Category` == "Hour51-70",
           Class == ">50K",
           
           ))[1]


dim(filter(Adult_NoMacro, 
           `EduNumEducation-Num` == "EduNum14",
           Occupation == "Exec-managerial",
           Race != "White",
           `Marital-status` != "Married-civ-spouse",
           Relationship != "Husband",
           `Age-Category` != "Age41-50",
           `Age-Category` != "Age51-60"
))[1]

#Race != "White",
#`Marital-status` != "Married-civ-spouse"
#Relationship != "Husband"
#`Age-Category` != "Age41-50"
#`Age-Category` != "Age51-60"