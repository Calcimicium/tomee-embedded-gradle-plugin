package fr.laboiteadodo.gradle;

import org.gradle.api.*;

public class TomeeEmbeddedPlugin implements Plugin<Project> {
	@Override
	public void apply(Project project) {
		project.getTasks().register("start", StartTask.class, task -> {
			task.setGroup("tomee");
		});
	}
}
