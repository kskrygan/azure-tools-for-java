/**
 * Copyright (c) Microsoft Corporation
 * <p/>
 * All rights reserved.
 * <p/>
 * MIT License
 * <p/>
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and
 * to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 * <p/>
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of
 * the Software.
 * <p/>
 * THE SOFTWARE IS PROVIDED *AS IS*, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO
 * THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
 * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.microsoft.intellij.actions;

import com.intellij.execution.BeforeRunTask;
import com.intellij.execution.ProgramRunnerUtil;
import com.intellij.execution.RunnerAndConfigurationSettings;
import com.intellij.execution.configurations.ConfigurationFactory;
import com.intellij.execution.configurations.ConfigurationType;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.execution.executors.DefaultRunExecutor;
import com.intellij.execution.impl.RunDialog;
import com.intellij.execution.impl.RunManagerImpl;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.microsoft.intellij.run.configuration.MavenWebAppConfigurationType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.idea.maven.project.MavenProjectsManager;
import org.jetbrains.idea.maven.tasks.MavenBeforeRunTask;

import java.io.File;
import java.util.List;

public class MavenWebDeployAction extends AnAction {

    private static final String MAVEN_TASK_PACKAGE = "package";
    private static final String POM_FILE_NAME = "pom.xml";
    private static final String DIALOG_TITLE = "Deploy to Azure";

    private ConfigurationType configType;

    public MavenWebDeployAction() {
        this.configType = MavenWebAppConfigurationType.getInstance();
    }

    @Override
    public void actionPerformed(AnActionEvent event) {
        Project project = event.getProject();
        if (project == null) {
            return;
        }

        ApplicationManager.getApplication().invokeLater(() -> {
            runConfiguration(project);
        });
    }

    private void runConfiguration(Project project) {
        final RunManagerImpl manager = RunManagerImpl.getInstanceImpl(project);
        RunnerAndConfigurationSettings settings = manager.findConfigurationByName(configType.getDisplayName());
        boolean shouldCreateBeforeRunTask = (settings == null);
        List<BeforeRunTask> beforeRunTasks = null;
        if (shouldCreateBeforeRunTask) {
            final ConfigurationFactory factory =
                    configType != null ? configType.getConfigurationFactories()[0] : null;
            settings = manager.createConfiguration(configType.getDisplayName(), factory);
            beforeRunTasks = getBeforeRunTasks(project, manager, settings.getConfiguration());
        }
        if (RunDialog.editConfiguration(project, settings, DIALOG_TITLE, DefaultRunExecutor.getRunExecutorInstance())) {
            if (shouldCreateBeforeRunTask) {
                manager.addConfiguration(settings, false /*isShared*/,
                        beforeRunTasks, false /*addEnabledTemplateTaskIfAbsent*/);
            }
            manager.setSelectedConfiguration(settings);
            ProgramRunnerUtil.executeConfiguration(project, settings, DefaultRunExecutor.getRunExecutorInstance());
        }
    }

    @NotNull
    private List<BeforeRunTask> getBeforeRunTasks(Project project, RunManagerImpl manager,
                                                  RunConfiguration configuration) {
        List<BeforeRunTask> beforeRunTasks = manager.getBeforeRunTasks(configuration);

        if (MavenProjectsManager.getInstance(project).isMavenizedProject()) {
            MavenBeforeRunTask task = new MavenBeforeRunTask();

            task.setProjectPath(project.getBasePath() + File.separator + POM_FILE_NAME);
            task.setGoal(MAVEN_TASK_PACKAGE);
            task.setEnabled(true);
            beforeRunTasks.add(task);

            manager.setBeforeRunTasks(configuration,
                    beforeRunTasks, false /*addEnabledTemplateTaskIfAbsent*/);
        }
        return beforeRunTasks;
    }

}