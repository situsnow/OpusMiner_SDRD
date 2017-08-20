# For Apriori
library(Matrix)
library(arules)

# visualize association rules
library(kernlab)
library(grid)
library(arulesViz)



findAssoRule = function(df, suppVal, confVal, rhsVec){
  assoRules = apriori(df,control = list(verbose = F),
                      parameter = list(minlen = 1, maxlen = 6, supp = suppVal, conf = confVal,
                                       target = "closed frequent itemsets"),
                      appearance = list(default = "lhs", rhs = rhsVec))
  assoRules.sorted = sort(assoRules, by = "lift")
  
  # find redundant rules
  # is.subset(), For rules, the union of lhs and rhs is used at the set of items.
  subset.matrix = is.subset(assoRules.sorted, assoRules.sorted)
  subset.matrix[lower.tri(subset.matrix, diag = T)] = NA
  redundantRules = colSums(subset.matrix, na.rm = T) >= 1
  #which(redundantRules)
  
  # remove redundant rules
  assoRules.pruned = assoRules.sorted[!redundantRules]
  #inspect(assoRules.pruned)
  return(assoRules.pruned)
}


consequent = c("field12=2")

#rule = findAssoRule(Adult_R, 0.005, 0.8, consequent)

library(dplyr)
Adult_R = Adult_R %>% mutate_if(is.character,as.factor)
Adult_R$field12 = as.factor(Adult_R$field12)
assoRules = apriori(Adult_R,control = list(verbose = F),
                    parameter = list(minlen = 1, maxlen = 6, supp = 0.005, conf = 0.8,
                                     target = "rules"),
                    appearance = list(default = "lhs", rhs = consequent))


assoRules.sorted = sort(assoRules, by = "lift")
test = as(assoRules.sorted, "data.frame")

test.unuque = unique.data.frame(test)

write.csv(test.unuque, 'output.csv')

measures = interestMeasure(assoRules, method = c("leverage","fishersExactTest"), 
                           transactions = Adult_R)

#hyperConfidence = interestMeasure(rules, measure = "hyperConfidence", 
#transactions = Income)
