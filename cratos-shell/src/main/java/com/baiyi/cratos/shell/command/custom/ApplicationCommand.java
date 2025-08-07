package com.baiyi.cratos.shell.command.custom;

import com.baiyi.cratos.common.table.PrettyTable;
import com.baiyi.cratos.common.util.GroupingUtils;
import com.baiyi.cratos.domain.util.StringFormatter;
import com.baiyi.cratos.eds.report.ListAppGroup;
import com.baiyi.cratos.eds.report.model.AppGroupSpec;
import com.baiyi.cratos.shell.PromptColor;
import com.baiyi.cratos.shell.SshShellHelper;
import com.baiyi.cratos.shell.SshShellProperties;
import com.baiyi.cratos.shell.annotation.ShellAuthentication;
import com.baiyi.cratos.shell.command.AbstractCommand;
import com.baiyi.cratos.shell.command.SshShellComponent;
import com.baiyi.cratos.shell.command.custom.executor.GroupingAppExecutor;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;
import org.springframework.shell.standard.ShellOption;
import org.springframework.util.StringUtils;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import static com.baiyi.cratos.shell.command.custom.ApplicationCommand.GROUP;


/**
 * @Author baiyi
 * @Date 2024/4/18 下午3:07
 * @Version 1.0
 */
@Slf4j
@SshShellComponent
@ShellCommandGroup("Application Commands")
@ConditionalOnProperty(name = SshShellProperties.SSH_SHELL_PREFIX + ".commands." + GROUP + ".create", havingValue = "true", matchIfMissing = true)
public class ApplicationCommand extends AbstractCommand {

    public static final String GROUP = "app";
    private static final String COMMAND_APP_GROUPING = GROUP + "-grouping";
    private static final String COMMAND_APP_GROUP_LIST = GROUP + "-group-list";
    private static final boolean flag = true;
    public final static String[] APP_GROUPING_REPORT_TABLE_FIELD_NAME = {"App Name", "Total", "Canary", "G1", "Replicas", "G2", "Replicas", "G3", "Replicas", "G4", "Replicas", "Specifications"};
    public final static String[] APP_GROUPING_SPECIFICATIONS_TABLE_FIELD_NAME = {"Total", "G1", "G2", "G3", "G4", "---", "Total", "G1", "G2", "G3", "G4", "---", "Total", "G1", "G2", "G3", "G4", "---", "Total", "G1", "G2", "G3", "G4"};

    private final ListAppGroup listAppGroup;
    private final GroupingAppExecutor groupingAppExecutor;

    public ApplicationCommand(SshShellHelper helper, SshShellProperties properties,
                              GroupingAppExecutor groupingAppExecutor, ListAppGroup deploymentSubGroupReport) {
        super(helper, properties, properties.getCommands()
                .getApplication());
        this.groupingAppExecutor = groupingAppExecutor;
        this.listAppGroup = deploymentSubGroupReport;
    }

    @ShellMethod(key = COMMAND_APP_GROUPING, value = "Grouping Application")
    @ShellMethodAvailability("appGroupingAvailability")
    @ShellAuthentication(resource = "/application/app-grouping")
    public void appGrouping(@ShellOption(help = "Application Name", defaultValue = "") String name,
                            @ShellOption(help = "Specify the total number of replicas for the group", defaultValue = "0") int replicas,
                            @ShellOption(help = "Preview group details", defaultValue = "false") boolean preview) {
        if (!StringUtils.hasText(name)) {
            helper.printError("Application name must be specified.");
            return;
        }
        Map<String, AppGroupSpec.GroupSpec> groupMap = listAppGroup.getGroupMap(name, true);

        // preview
        if (preview && StringUtils.hasText(name)) {
            PrettyTable table = PrettyTable.fieldNames(APP_GROUPING_REPORT_TABLE_FIELD_NAME);
            AtomicReference<Integer> sum = new AtomicReference<>(0);
            groupMap.keySet()
                    .forEach(k -> {
                        final AppGroupSpec.GroupSpec e = groupMap.get(k);

                        final String rowAppName = getDyeingAppName(e.getAppName(), e.countTotalReplicas());
                        final String rowTotal = getDyeingReplicas(e.countTotalReplicas());
                        final String rowCanary = Optional.ofNullable(e.getCanary())
                                .map(AppGroupSpec.Group::getName)
                                .orElse("");

                        final String rowG1 = e.getG1() != null ? e.getG1()
                                .getName() : "";
                        final String rowG1Replicas = e.getG1() != null ? getDyeingReplicas(e.getG1()
                                .getReplicas()) : "";

                        final String rowG2 = e.getG2() != null ? e.getG2()
                                .getName() : "";
                        final String rowG2Replicas = e.getG2() != null ? getDyeingReplicas(e.getG2()
                                .getReplicas()) : "";

                        final String rowG3 = e.getG3() != null ? e.getG3()
                                .getName() : "";
                        final String rowG3Replicas = e.getG3() != null ? getDyeingReplicas(e.getG3()
                                .getReplicas()) : "";

                        final String rowG4 = e.getG4() != null ? e.getG4()
                                .getName() : "";
                        final String rowG4Replicas = e.getG4() != null ? getDyeingReplicas(e.getG4()
                                .getReplicas()) : "";
                        table.addRow(rowAppName, rowTotal, rowCanary, rowG1, rowG1Replicas, rowG2, rowG2Replicas, rowG3,
                                rowG3Replicas, rowG4, rowG4Replicas, getSpecifications(replicas, e));
                    });
            helper.print(table.toString());
            return;
        }
        groupingAppExecutor.doGrouping(groupMap.get(name), name, replicas);
    }

    @ShellMethod(key = COMMAND_APP_GROUP_LIST, value = "List Application Group")
    @ShellMethodAvailability("appGroupListAvailability")
    public void appGroupList(@ShellOption(help = "Application Name", defaultValue = "") String name,
                             @ShellOption(help = "Display Application Grouping Specifications", defaultValue = "false") boolean specifications,
                             @ShellOption(help = "Filter Invalid Applications", defaultValue = "false") boolean filter) {
        if (specifications) {
            displayApplicationGroupingSpecifications();
            return;
        }
        helper.print("The total number does not include canary.", PromptColor.GREEN);
        PrettyTable table = PrettyTable.fieldNames(APP_GROUPING_REPORT_TABLE_FIELD_NAME);
        Map<String, AppGroupSpec.GroupSpec> groupingMap = listAppGroup.getGroupMap(name, false);
        AtomicReference<Integer> sum = new AtomicReference<>(0);
        groupingMap.keySet()
                .forEach(k -> {
                    final AppGroupSpec.GroupSpec e = groupingMap.get(k);

                    final String rowAppName = getDyeingAppName(e.getAppName(), e.countTotalReplicas());
                    final String rowTotal = getDyeingReplicas(e.countTotalReplicas());
                    final String rowCanary = Optional.ofNullable(e.getCanary())
                            .map(AppGroupSpec.Group::getName)
                            .orElse("");

                    final String rowG1 = e.getG1() != null ? e.getG1()
                            .getName() : "";
                    final String rowG1Replicas = e.getG1() != null ? getDyeingReplicas(e.getG1()
                            .getReplicas()) : "";

                    final String rowG2 = e.getG2() != null ? e.getG2()
                            .getName() : "";
                    final String rowG2Replicas = e.getG2() != null ? getDyeingReplicas(e.getG2()
                            .getReplicas()) : "";

                    final String rowG3 = e.getG3() != null ? e.getG3()
                            .getName() : "";
                    final String rowG3Replicas = e.getG3() != null ? getDyeingReplicas(e.getG3()
                            .getReplicas()) : "";

                    final String rowG4 = e.getG4() != null ? e.getG4()
                            .getName() : "";
                    final String rowG4Replicas = e.getG4() != null ? getDyeingReplicas(e.getG4()
                            .getReplicas()) : "";

                    if (!filter || e.countTotalReplicas() != 0) {
                        table.addRow(rowAppName, rowTotal, rowCanary, rowG1, rowG1Replicas, rowG2, rowG2Replicas, rowG3,
                                rowG3Replicas, rowG4, rowG4Replicas, getSpecifications(e));
                    }
                    sum.set(sum.get() + e.countTotalReplicas());
                });
        helper.print(table.toString());
        helper.print(StringFormatter.arrayFormat("{} Results, Total number of replicas {}", table.getRows().size(), sum),
                PromptColor.GREEN);
    }

    private String getDyeingAppName(String appName, int totalReplicas) {
        if (totalReplicas == 0) {
            return helper.getBackgroundColored(appName, PromptColor.RED);
        } else {
            return helper.getColored(appName, PromptColor.GREEN);
        }
    }

    private String getDyeingReplicas(int replicas) {
        if (replicas == 0) {
            return helper.getColored(String.valueOf(replicas), PromptColor.RED);
        } else {
            return String.valueOf(replicas);
        }
    }

    private String getSpecifications(int total, AppGroupSpec.GroupSpec groupingSpecifications) {
        if (total == 0) {
            total = groupingSpecifications.countTotalReplicas();
        }
        List<String> desc = Lists.newArrayList();
        List<Integer> groups = Lists.newArrayList();
        GroupingUtils.grouping(total, groups);
        groups = groups.stream()
                .sorted(Comparator.comparingInt(Integer::intValue))
                .toList();
        desc.add(getSpecifications(groupingSpecifications.getG1(), !groups.isEmpty() ? groups.get(0) : 0));
        desc.add(getSpecifications(groupingSpecifications.getG2(), groups.size() > 1 ? groups.get(1) : 0));
        desc.add(getSpecifications(groupingSpecifications.getG3(), groups.size() > 2 ? groups.get(2) : 0));
        desc.add(getSpecifications(groupingSpecifications.getG4(), groups.size() > 3 ? groups.get(3) : 0));
        return Joiner.on(",")
                .join(desc);
    }

    private String getSpecifications(AppGroupSpec.GroupSpec groupingSpecifications) {
        return getSpecifications(groupingSpecifications.countTotalReplicas(), groupingSpecifications);
    }

    private String getSpecifications(AppGroupSpec.Group grouping, int specifications) {
        if (grouping == null) {
            if (specifications == 0) {
                return helper.getColored(String.valueOf(specifications), PromptColor.GREEN);
            } else {
                return helper.getColored(String.valueOf(specifications), PromptColor.RED);
            }
        }
        if (grouping.getReplicas() == specifications) {
            return helper.getColored(String.valueOf(specifications), PromptColor.GREEN);
        } else {
            return helper.getColored(String.valueOf(specifications), PromptColor.RED);
        }
    }

    private void displayApplicationGroupingSpecifications() {
        PrettyTable table = PrettyTable.fieldNames(APP_GROUPING_SPECIFICATIONS_TABLE_FIELD_NAME);
        final String notApplicable = helper.getColored("0", PromptColor.RED);
        int loop = 25;
        for (int i = 1; i <= loop; i++) {
            List<Integer> groups1 = Lists.newArrayList();
            GroupingUtils.grouping(i, groups1);
            groups1 = groups1.stream()
                    .sorted(Comparator.comparingInt(Integer::intValue))
                    .toList();

            List<Integer> groups2 = Lists.newArrayList();
            GroupingUtils.grouping(i + loop, groups2);
            groups2 = groups2.stream()
                    .sorted(Comparator.comparingInt(Integer::intValue))
                    .toList();

            List<Integer> groups3 = Lists.newArrayList();
            GroupingUtils.grouping(i + loop * 2, groups3);
            groups3 = groups3.stream()
                    .sorted(Comparator.comparingInt(Integer::intValue))
                    .toList();

            List<Integer> groups4 = Lists.newArrayList();
            GroupingUtils.grouping(i + loop * 3, groups4);
            groups4 = groups4.stream()
                    .sorted(Comparator.comparingInt(Integer::intValue))
                    .toList();
            // 不要动
            table.addRow(helper.getColored(String.valueOf(i), PromptColor.GREEN), groups1.get(0),
                    groups1.size() > 1 ? groups1.get(1) : notApplicable,
                    groups1.size() > 2 ? groups1.get(2) : notApplicable,
                    groups1.size() > 3 ? groups1.get(3) : notApplicable, "",
                    helper.getColored(String.valueOf(i + loop), PromptColor.GREEN), groups2.get(0),
                    groups2.size() > 1 ? groups2.get(1) : notApplicable,
                    groups2.size() > 2 ? groups2.get(2) : notApplicable,
                    groups2.size() > 3 ? groups2.get(3) : notApplicable, "",
                    helper.getColored(String.valueOf(i + loop * 2), PromptColor.GREEN), groups3.get(0),
                    groups3.size() > 1 ? groups3.get(1) : notApplicable,
                    groups3.size() > 2 ? groups3.get(2) : notApplicable,
                    groups3.size() > 3 ? groups3.get(3) : notApplicable, "",
                    helper.getColored(String.valueOf(i + loop * 3), PromptColor.GREEN), groups4.get(0),
                    groups4.size() > 1 ? groups4.get(1) : notApplicable,
                    groups4.size() > 2 ? groups4.get(2) : notApplicable,
                    groups4.size() > 3 ? groups4.get(3) : notApplicable);
        }
        helper.print(table.toString());
    }

}