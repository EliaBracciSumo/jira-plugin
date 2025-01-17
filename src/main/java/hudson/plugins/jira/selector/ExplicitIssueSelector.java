package hudson.plugins.jira.selector;

import hudson.EnvVars;
import hudson.Extension;
import hudson.model.Descriptor;
import hudson.model.Run;
import hudson.model.TaskListener;
import hudson.plugins.jira.EnvironmentExpander;
import hudson.plugins.jira.JiraSite;
import hudson.plugins.jira.Messages;
import org.apache.commons.lang.StringUtils;
import org.kohsuke.stapler.DataBoundConstructor;

import edu.umd.cs.findbugs.annotations.CheckForNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ExplicitIssueSelector extends AbstractIssueSelector {

    @CheckForNull
    private List<String> jiraIssueKeys;
    private String issueKeys;

    @DataBoundConstructor
    public ExplicitIssueSelector(String issueKeys) {
        this.jiraIssueKeys = StringUtils.isNotBlank(issueKeys) ? Arrays.asList(issueKeys.split(",")) : Collections.emptyList();
        this.issueKeys = issueKeys;
    }

    public ExplicitIssueSelector(List<String> jiraIssueKeys) {
        this.jiraIssueKeys = jiraIssueKeys;
    }

    public ExplicitIssueSelector(){
        this.jiraIssueKeys = Collections.emptyList();
    }

    public void setIssueKeys(String issueKeys){
        this.issueKeys = issueKeys;
        this.jiraIssueKeys = StringUtils.isNotBlank(issueKeys) ? Arrays.asList(issueKeys.split( ",")):new ArrayList<>( );
    }

    public String getIssueKeys(){
        return issueKeys;
    }

    @Override
    public Set<String> findIssueIds(Run<?, ?> run, JiraSite site, TaskListener listener) {
        EnvVars envVars = EnvironmentExpander.getEnvVars(run, listener);

        List<String> issueKeys = new ArrayList<>();
        for (String issue : jiraIssueKeys) {
            issueKeys.add(EnvironmentExpander.expandVariable(issue, envVars));
        }

        return new HashSet(issueKeys);
    }

    @Extension
    public static final class DescriptorImpl extends Descriptor<AbstractIssueSelector> {
        @Override
        public String getDisplayName() {
            return Messages.IssueSelector_ExplicitIssueSelector_DisplayName();
        }
    }

}
