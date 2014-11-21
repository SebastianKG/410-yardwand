YARDWAND Testing README


We tried our best to implement as many test as we could to ensure each part of our application is working properly and flawlessly.  In below we have described each test class in details:

NewCollaboratorDetectorTest: Ensures that our collaborator/commit analyser is working properly and retrieving json from GitHub API and populate our system with proper data.

CodeDuplicatorDetectorTest: Ensures that our code duplication detector class runs properly and handles any invalid inputs to methods

CommandLineTest: Tests to see if java runtime commands work to locate the directory of our bloat/duplication analyser

MetricComparatorTest: Ensures that MetricComparator.java is behaving as expected. It runs the MetricComparator, builds the Stats, and prints output so that we may check that the numbers generated are what we expect ie. velocities, weights, collaboration activity

ProcessBuilderTest: We test our ProcessBuilder in this class and make sure that it can execute simian commands properly and in the right order.

RepositoryTest: Ensures that Repository.java is working properly. It checks for all types of URL and web protocols to be correct.

SimianOutputParserTest: Ensures that SimianOutputParser.java provides a well formed and correct output. We are testing all conditions such as having Null cases and Line Breaks in the input.

Application.javaTest: Since Application is consist of HTML/CSS and images we had to test this module visually and manually. It was not possible to provide a jUnit test.

