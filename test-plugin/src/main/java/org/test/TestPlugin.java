package org.test;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

public class TestPlugin implements Plugin<Project> {

  @Override
  public void apply(Project project){
    System.out.println(String.format("TestPlugin being applied to project: %s", project));
  }
}