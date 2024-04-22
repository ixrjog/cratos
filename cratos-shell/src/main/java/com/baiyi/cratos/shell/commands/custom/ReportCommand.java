package com.baiyi.cratos.shell.commands.custom;

import com.baiyi.cratos.common.table.PrettyTable;
import com.baiyi.cratos.common.util.GroupingUtil;
import com.baiyi.cratos.common.util.StringFormatter;
import com.baiyi.cratos.eds.report.DeploymentSubGroupReport;
import com.baiyi.cratos.eds.report.model.AppGroupingSpecifications;
import com.baiyi.cratos.shell.PromptColor;
import com.baiyi.cratos.shell.SshShellHelper;
import com.baiyi.cratos.shell.SshShellProperties;
import com.baiyi.cratos.shell.commands.AbstractCommand;
import com.baiyi.cratos.shell.commands.SshShellComponent;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;
import org.springframework.shell.standard.ShellOption;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import static com.baiyi.cratos.shell.commands.custom.HostAssetCommand.GROUP;

/**
 * @Author baiyi
 * @Date 2024/4/18 下午3:07
 * @Version 1.0
 */
@Slf4j
@SshShellComponent
@ShellCommandGroup("Report Commands")
@ConditionalOnProperty(name = SshShellProperties.SSH_SHELL_PREFIX + ".commands." + GROUP + ".create", havingValue = "true", matchIfMissing = true)
public class ReportCommand extends AbstractCommand {

    public static final String GROUP = "report";
    private static final String COMMAND_APP_GROUPING = GROUP + "-app-grouping";
    private static final String COMMAND_APP_GROUPING_SPECIFICATIONS = GROUP + "-app-grouping-specifications";

    @Resource
    private DeploymentSubGroupReport deploymentSubGroupReport;

    public ReportCommand(SshShellHelper helper, SshShellProperties properties) {
        super(helper, properties, properties.getCommands()
                .getReport());
    }

    private static final boolean flag = true;

    public final static String[] APP_GROUPING_REPORT_TABLE_FIELD_NAME = {"App Name", "Total", "Canary", "G1", "Replicas", "G2", "Replicas", "G3", "Replicas", "G4", "Replicas", "Specifications"};

    public final static String[] APP_GROUPING_SPECIFICATIONS_TABLE_FIELD_NAME = {"Total", "G1", "G2", "G3", "G4", "---", "Total", "G1", "G2", "G3", "G4", "---", "Total", "G1", "G2", "G3", "G4", "---", "Total", "G1", "G2", "G3", "G4"};

    @ShellMethod(key = COMMAND_APP_GROUPING, value = "Report app grouping")
    @ShellMethodAvailability("appGroupingReportAvailability")
    public void appGroupingReport(@ShellOption(help = "Application Name", defaultValue = "") String name, @ShellOption(help = "Display Application Grouping Specifications", defaultValue = "false") boolean specifications) {
        if (specifications) {
            displayApplicationGroupingSpecifications();
            return;
        }
        helper.print("The total number does not include canary.", PromptColor.GREEN);
        PrettyTable table = PrettyTable.fieldNames(APP_GROUPING_REPORT_TABLE_FIELD_NAME);
        Map<String, AppGroupingSpecifications.GroupingSpecifications> groupingMap = deploymentSubGroupReport.getGroupingMap(name);
        AtomicReference<Integer> sum = new AtomicReference<>(0);
        groupingMap.keySet()
                .forEach(k -> {
                    AppGroupingSpecifications.GroupingSpecifications e = groupingMap.get(k);
                    table.addRow(e.getAppName(), e.countTotalReplicas(), e.getCanary() != null ? e.getCanary()
                            .getName() : "", e.getG1() != null ? e.getG1()
                            .getName() : "", e.getG1() != null ? e.getG1()
                            .getReplicas() : "", e.getG2() != null ? e.getG2()
                            .getName() : "", e.getG2() != null ? e.getG2()
                            .getReplicas() : "", e.getG3() != null ? e.getG3()
                            .getName() : "", e.getG3() != null ? e.getG3()
                            .getReplicas() : "", e.getG4() != null ? e.getG4()
                            .getName() : "", e.getG4() != null ? e.getG4()
                            .getReplicas() : "", getSpecifications(e));
                    sum.set(sum.get() + e.countTotalReplicas());
                });
        helper.print(table.toString());
        helper.print(StringFormatter.arrayFormat("{} Results, Total number of replicas {}", table.rows.size(), sum), PromptColor.GREEN);
    }

    private String getSpecifications(AppGroupingSpecifications.GroupingSpecifications groupingSpecifications) {
        List<String> desc = Lists.newArrayList();
        int total = groupingSpecifications.countTotalReplicas();
        List<Integer> groups = Lists.newArrayList();
        GroupingUtil.grouping(total, groups);
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

    private String getSpecifications(AppGroupingSpecifications.Grouping grouping, int specifications) {
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
            GroupingUtil.grouping(i, groups1);
            groups1 = groups1.stream()
                    .sorted(Comparator.comparingInt(Integer::intValue))
                    .toList();

            List<Integer> groups2 = Lists.newArrayList();
            GroupingUtil.grouping(i + loop, groups2);
            groups2 = groups2.stream()
                    .sorted(Comparator.comparingInt(Integer::intValue))
                    .toList();

            List<Integer> groups3 = Lists.newArrayList();
            GroupingUtil.grouping(i + loop * 2, groups3);
            groups3 = groups3.stream()
                    .sorted(Comparator.comparingInt(Integer::intValue))
                    .toList();

            List<Integer> groups4 = Lists.newArrayList();
            GroupingUtil.grouping(i + loop * 3, groups4);
            groups4 = groups4.stream()
                    .sorted(Comparator.comparingInt(Integer::intValue))
                    .toList();
            // 不要动
            table.addRow(helper.getColored(String.valueOf(i), PromptColor.GREEN), groups1.get(0), groups1.size() > 1 ? groups1.get(1) : notApplicable, groups1.size() > 2 ? groups1.get(2) : notApplicable, groups1.size() > 3 ? groups1.get(3) : notApplicable, "", helper.getColored(String.valueOf(i + loop), PromptColor.GREEN), groups2.get(0), groups2.size() > 1 ? groups2.get(1) : notApplicable, groups2.size() > 2 ? groups2.get(2) : notApplicable, groups2.size() > 3 ? groups2.get(3) : notApplicable, "", helper.getColored(String.valueOf(i + loop * 2), PromptColor.GREEN), groups3.get(0), groups3.size() > 1 ? groups3.get(1) : notApplicable, groups3.size() > 2 ? groups3.get(2) : notApplicable, groups3.size() > 3 ? groups3.get(3) : notApplicable, "", helper.getColored(String.valueOf(i + loop * 3), PromptColor.GREEN), groups4.get(0), groups4.size() > 1 ? groups4.get(1) : notApplicable, groups4.size() > 2 ? groups4.get(2) : notApplicable, groups4.size() > 3 ? groups4.get(3) : notApplicable);
        }
        helper.print(table.toString());
    }

}