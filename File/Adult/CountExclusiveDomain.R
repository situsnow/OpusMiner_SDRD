#28495
dim(filter(Adult_NoMacro, Occupation != "Exec-managerial",
           `Hours-Category` != "Hour36-50"))[1]

#33
dim(filter(Adult_NoMacro, Occupation != "Exec-managerial",
           `Hours-Category` != "Hour36-50",
           Class == ">50K",
           `Age-Category` == "Age41-50",
           `EduNumEducation-Num` == "EduNum15",
           Workclass == "Self-emp-inc"))[1]


dim(filter(Adult_NoMacro, `Hours-Category` != "Hour36-50"))[1]

#5873
dim(filter(Adult_NoMacro, Occupation != "Exec-managerial", 
           `Hours-Category` != "Hour36-50",
           Class == ">50K"))[1]

#34
dim(filter(Adult_NoMacro, Occupation != "Exec-managerial", 
           `Hours-Category` != "Hour36-50",
           `Age-Category` == "Age41-50",
           `EduNumEducation-Num` == "EduNum15",
           Workclass == "Self-emp-inc"))[1]

#5794
dim(filter(Adult_NoMacro, Occupation != "Exec-managerial", 
           `Hours-Category` != "Hour36-50",
           `Age-Category` == "Age41-50"))[1]

#74
dim(filter(Adult_NoMacro, Occupation != "Exec-managerial", 
           `Hours-Category` != "Hour36-50",
           Class == ">50K",
           `EduNumEducation-Num` == "EduNum15",
           Workclass == "Self-emp-inc"))[1]

#524
dim(filter(Adult_NoMacro, Occupation != "Exec-managerial",
           `Hours-Category` != "Hour36-50",
           `EduNumEducation-Num` == "EduNum15"))[1]

#135
dim(filter(Adult_NoMacro, Occupation != "Exec-managerial", 
           `Hours-Category` != "Hour36-50",
           Class == ">50K",
           `Age-Category` == "Age41-50",
           Workclass == "Self-emp-inc"))[1]

#716
dim(filter(Adult_NoMacro, Occupation != "Exec-managerial", 
           `Hours-Category` != "Hour36-50",
           Workclass == "Self-emp-inc"))[1]

#150
dim(filter(Adult_NoMacro, Occupation != "Exec-managerial", 
           `Hours-Category` != "Hour36-50",
           Class == ">50K",
           `Age-Category` == "Age41-50",
           `EduNumEducation-Num` == "EduNum15"))[1]

#1951
dim(filter(Adult_NoMacro, Occupation != "Exec-managerial",
           `Hours-Category` != "Hour36-50",
           Class == ">50K",
           `Age-Category` == "Age41-50"))[1]

#76
dim(filter(Adult_NoMacro, Occupation != "Exec-managerial",
           `Hours-Category` != "Hour36-50",
           `EduNumEducation-Num` == "EduNum15",
           Workclass == "Self-emp-inc"))[1]

# 385
dim(filter(Adult_NoMacro, Occupation != "Exec-managerial", 
           `Hours-Category` != "Hour36-50",
           Class == ">50K",
           `EduNumEducation-Num` == "EduNum15"))[1]

#233
dim(filter(Adult_NoMacro, Occupation != "Exec-managerial", 
           `Hours-Category` != "Hour36-50",
           `Age-Category` == "Age41-50",
           Workclass == "Self-emp-inc"))[1]


#368
dim(filter(Adult_NoMacro, Occupation != "Exec-managerial", 
           `Hours-Category` != "Hour36-50",
           Class == ">50K",
           Workclass == "Self-emp-inc"))[1]

#173
dim(filter(Adult_NoMacro, Occupation != "Exec-managerial", 
           `Hours-Category` != "Hour36-50",
           `Age-Category` == "Age41-50",
           `EduNumEducation-Num` == "EduNum15"))[1]
#################################################################

dim(filter(Adult_NoMacro, Class == ">50K", `Marital-status` == "Married-civ-spouse", Relationship == "Husband"))[1]
           

dim(filter(Adult_NoMacro, Relationship == "Husband", Sex == "Male"))[1]
           

dim(filter(Adult_NoMacro, Class == ">50K", Relationship == "Husband",Occupation == "Exec-managerial"))[1]

#################################################################
#Consequent : 7841
dim(filter(Adult_NoMacro, Class == ">50K"))[1]

7841/32561

#27816
dim(filter(Adult_NoMacro, Race == "White"))[1]

#Total: 32561

dim(filter(Adult_NoMacro, `Marital-status` == "Married-civ-spouse", Race == "White", Class == ">50K"))[1]

#Actual leverage
(6105/32561) - (13410/32561) * (7841/32561)

#upper bound
27816/32561

#################################################################
#Non-self-sufficient itemset: 1276
dim(filter(Adult_NoMacro, Relationship == "Husband", 
           Occupation == "Prof-specialty", Class == ">50K"))[1]

#Antecedent: 1804
dim(filter(Adult_NoMacro, Relationship == "Husband", 
           Occupation == "Prof-specialty"))[1]

#Consequent : 7841
dim(filter(Adult_NoMacro, Class == ">50K"))[1]


`Marital-status` == "Married-civ-spouse"
Race == "White"