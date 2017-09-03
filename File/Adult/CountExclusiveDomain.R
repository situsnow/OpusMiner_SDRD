
dim(filter(Adult_R,
           field12 == "2", #consequent
           field11 != "United-States" #10
           ))[1]

dim(filter(Adult_R,
           field11 != "United-States" #10
))[1]

field12 == "2", #consequent
field8 == "White", #7
field9 == "Male", #8
field1 == "Age41-50", #12
field5 == "Married-civ-spouse", #14
field6 == "Exec-managerial", #15
field3 == "Masters", #32
