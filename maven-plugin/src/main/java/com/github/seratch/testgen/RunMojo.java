package com.github.seratch.testgen;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import com.github.seratch.testgen.*;

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


  public void execute() throws MojoExecutionException {

    // configure
    System.setProperty("testgen.encoding", encoding);
    System.setProperty("testgen.srcDir", srcDir);
    System.setProperty("testgen.srcTestDir", srcTestDir);

    // target paramter
    String target = System.getProperty("target");
    if (target == null) {
      throw new MojoExecutionException("[usage] mvn testgen:run -Dtarget=com.example.App");
    }

    try {
      Command.main(new String[] { target });
    } catch (Exception e) {
      throw new MojoExecutionException("testgen execute error..", e);
    }

  }

}
