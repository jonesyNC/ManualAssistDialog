# ManualAssistDialog

It is not always desirable or cost effective to automate all areas of an application under test. 

This stand alone Java application can be called from your test automation code and will allow you to prompt the user to perform manual test steps.

The idea behind this stand-alone application is that your test automation can do all the setup necessary for a test,
and then display a GUI dialog prompting the person running the test automation to manually perform steps via the application GUI.

The manual tester will then click on one of several buttons to indicate what should happen next.

A status code is returned to the calling application.
	     * 0 = Test Passed button selected
	     * 1 = Test Failed, but continue button selected
	     * 2 = Test Failed, stop executing button selected
	     * 3 = No button selected, user closed window.

Upon receiving the return code your test automation code can perform additional actions such as verifications or just set the test status based on the returned status code.

Because this is a Java application it will work on all platforms that support Java.

Required: Open JDK 11
