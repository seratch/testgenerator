package testgen;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import testgen.*;

/**
 * This goadl will process "testgen"
 *
 * @goal run
 * @phase process-sources
 */
public class RunMojo extends AbstractMojo {

  /**
   * @parameter
   */
  protected String encoding = "UTF-8";

  /**
   * @parameter
   */
  protected String srcDir = "src/main/scala";

  /**
   * @parameter
   */
  protected String srcTestDir = "src/test/scala";

  /**
   * @parameter
   */
  protected String testTemplate = "scalatest.FunSuite";

  /**
   * @parameter
   */
  protected String scalatest_Matchers = "ShouldMatchers";

  public void execute() throws MojoExecutionException {

    // configure
    System.setProperty("testgen.encoding", encoding);
    System.setProperty("testgen.srcDir", srcDir);
    System.setProperty("testgen.srcTestDir", srcTestDir);
    System.setProperty("testgen.testTemplate", testTemplate);
    System.setProperty("testgen.scalatest.Matchers", scalatest_Matchers);

    // target paramter
    String target = System.getProperty("target");
    if (target == null) {
      throw new MojoExecutionException("[usage] mvn testgen:run -Dtarget=com.example.App");
    }

    try {
      MainCommand.main(new String[] { target });
    } catch (Exception e) {
      throw new MojoExecutionException("testgen execute error...", e);
    }

  }

}
