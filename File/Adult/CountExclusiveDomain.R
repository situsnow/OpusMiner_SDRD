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
