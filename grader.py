










#!/usr/bin/python

import os,sys

def fatal():
    sys.exit()
def compileJava(filename, fileExt, classpath='.', verbose=True):
    if verbose:
        print "Compiling %s ..." % filename
    c = os.system("javac " + filename + classpath + fileExt)
    if (c != 0):
        print "Compiling %s failed with error code %d. Exiting." % (filename,c)
	fatal()

class grader:
    """A grader class"""

    # the constructor for the grader
    def __init__(self, javaClasses, test, subDirectory, resDirectory):
	self.javaClasses = javaClasses
	self.test = test
	self.subDirectory = subDirectory
	self.resDirectory = resDirectory
        self.testCounter = 0
	self.homeDir = os.getcwd()
	self.grade=0

    # compile submitted Java files
    def compileSubmission(self):
       # os.chdir(self.homeDir)
	i = 0
	counter = len(self.javaClasses)
	os.chdir(self.subDirectory)
	while (i < counter):
		compileJava(self.javaClasses[i], "java")
		i += 1
	
		

    # generate a test driver
    def generateTestDriver(self):
        os.chdir(self.homeDir)
	os.chdir(self.subDirectory)
	testDriverClass = open("TestDriver.java", "w")
	testDriverClass.write("public class TestDriver{public static void main (String args[]){ Parser parser = new Parser("+'"'+self.test[self.testCounter][0]+'"'+"); parser.run(); }}")
	testDriverClass.close()
	compileJava("TestDriver", "java")
	testFile = "test%d"%(self.testCounter + 1)+".txt"
	test=open(testFile,"w")
	os.system("java TestDriver>>"+testFile)
	os.system("mv " + testFile +" " +self.homeDir+"/"+self.resDirectory)
	
	

    # run one test, return true if it succeeds, false otherwise.
    def runOneTest(self):
        self.generateTestDriver()
	os.chdir(self.homeDir)
	os.chdir(self.resDirectory)
	testFile = "test%d"%(self.testCounter+1)+".txt"
	resFile = "test%d" % (self.testCounter+1)+".res"
	i = os.system("diff -b " + testFile+" "+ resFile)

	if i == 0:
		testReport = open("testReport.txt", "a")
		testReport.write("test "+str(self.testCounter+1)+"\nSuccess.\n")
		testReport.close()
		self.grade+=2
	else:
		testReport = open("testReport.txt", "a")
		testReport.write("test "+str(self.testCounter+1)+"\nFailure. Grade cannot be determined\n")
		fileDifference = open(testFile, "r")
		for line in fileDifference:
			testReport.write(line)
		fileDifference.close()
		testReport.close()
		

    # run all tests; put the result into resultList;
    def runTests(self):
	myCounter = len(self.test)
	while(self.testCounter < myCounter):
		self.runOneTest()
		self.testCounter+=1

    # generate the test report
    def generateReport(self):
        testReport = open("testReport.txt", "r")
	for line in testReport:
		print line
	print "Student's grade is "+str(self.grade)

# the main program follows

tests = [
('SELECT col1, c99 FROM tab1, t2, t9', 'test1.res', 2),
('SELECT c1, c2 FROM t1, t2 WHERE c3 = 7', 'test2.res', 2),
('SELECT c1,c2a,c3b2 FROM t1,t2,t3 WHERE c2>2.78 AND c2<5.9245 AND c3=3', 'test3.res', 2),
('SELECT col FROM tab1, tab2, WHERE colb=5', 'test4.res', 2),
('SELECT c1,c2 FROM t2 WHERE c1=2,c2=3', 'test5.res', 2)
]

if (len(sys.argv) > 1):
    submissionDir = sys.argv[1]
else: submissionDir = 'sampleSubmission'

if (len(sys.argv) > 2):
    oracleDir = sys.argv[2]
else: oracleDir = 'expectedResult'


g = grader(['Token', 'Lexer', 'Parser'], tests, submissionDir, oracleDir)
g.compileSubmission()
g.runTests()
g.generateReport()
