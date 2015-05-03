package tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ Flag_Test.class, Model_Test.class, Perception_Test.class })
public class CtFTestSuite { }
