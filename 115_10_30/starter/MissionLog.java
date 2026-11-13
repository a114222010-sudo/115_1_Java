import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class MissionLog {

    public static void main(String[] args) {
        int[] script = {1, 1, 2}; // 探索 → 探索 → 掃描。
        runMission(script, 20, 0);
    }

    static void runMission(int[] script, int energy, int samples) {
        int actionCount = 0;
        boolean running = true;
        ArrayList<Integer> commandLog = new ArrayList<>();
        ArrayList<Integer> energyLog = new ArrayList<>();
        ArrayList<String> resultLog = new ArrayList<>();

        for (int i = 0; i < script.length && running && energy > 0 && samples < 5; i++) {
            int command = script[i];
            String result;

            switch (command) {
                case 1: // 指令 1：探索，消耗 6 格能源，取得 2 份樣本。
                    if (energy >= 6) {
                        energy -= 6;
                        samples += 2;
                        actionCount++;
                        result = "成功";
                    } else {
                        result = "能源不足";
                    }
                    break;
                case 2: // 指令 2：掃描，消耗 2 格能源，取得 1 份樣本。
                    if (energy >= 2) {
                        energy -= 2;
                        samples++;
                        actionCount++;
                        result = "成功";
                    } else {
                        result = "能源不足";
                    }
                    break;
                case 3: // 指令 3：充電，增加 5 格能源，最多 20 格；已滿時拒絕充電。
                    if (energy < 20) {
                        energy += 5;
                        if (energy > 20) {
                            energy = 20;
                        }
                        actionCount++;
                        result = "成功";
                    } else {
                        result = "能源已滿";
                    }
                    break;
                case 0: // 指令 0：返回，停止讀取後續指令，不增加成功行動次數。
                    running = false;
                    result = "返回";
                    break;
                default: // 其他整數：無效指令，不改變能源、樣本或成功行動次數。
                    result = "無效指令";
                    break;
            }

            // 【已完成 TODO 2】把 command、energy、result 加到對應清單
            commandLog.add(command);
            energyLog.add(energy);
            resultLog.add(result);
        }

        String outcome;
        if (samples >= 5) {
            outcome = "任務成功";
        } else if (energy == 0) {
            outcome = "能源耗盡";
        } else if (!running) {
            outcome = "已返回";
        } else {
            outcome = "任務未完成";
        }

        printReplay(commandLog, energyLog, resultLog);
        System.out.println("指令次數[0..3]=" + Arrays.toString(countCommands(commandLog)));
        printSummary(script.length, energyLog, resultLog);
        System.out.println("最終能源=" + energy + ", 樣本數=" + samples + ", 行動次數=" + actionCount + " -> " + outcome);
        System.out.println("電量百分比=" + calculateBatteryPercent(energy));
    }

    static int[] countCommands(ArrayList<Integer> commandLog) {
        int[] counts = new int[4];
        // 【已完成 TODO 1】走訪 commandLog；指令在 0..3 才增加 counts[command]
        for (int command : commandLog) {
            if (command >= 0 && command <= 3) {
                counts[command]++;
            }
        }
        return counts;
    }

    static void printReplay(ArrayList<Integer> commandLog, ArrayList<Integer> energyLog, ArrayList<String> resultLog) {
        System.out.println("=== 任務回放 ===");
        // 【已完成 TODO 3】用相同索引讀三條清單
        for (int i = 0; i < commandLog.size(); i++) {
            System.out.println("第 " + (i + 1) + " 步: 指令=" + commandLog.get(i) + ", 結果=" + resultLog.get(i) + ", 剩餘能源=" + energyLog.get(i));
        }
    }

    static void printSummary(int scriptLength, ArrayList<Integer> energyLog, ArrayList<String> resultLog) {
        // 【已完成 TODO 4】用 frequency 計算 成功、返回，印出 steps、ok、rejected、ignored
        int steps = resultLog.size();
        int ok = Collections.frequency(resultLog, "成功");
        int returned = Collections.frequency(resultLog, "返回");
        int rejected = steps - ok - returned;
        int ignored = scriptLength - steps;
        System.out.println("摘要: steps=" + steps + ", ok=" + ok + ", rejected=" + rejected + ", ignored=" + ignored);

        // 【已完成 TODO 5】先用 isEmpty 判斷；空清單印 無紀錄，否則用 min 印最小能源
        if (energyLog.isEmpty()) {
            System.out.println("最低能源=無紀錄");
        } else {
            System.out.println("最低能源=" + Collections.min(energyLog));
        }
    }

    static double calculateBatteryPercent(int energy) {
        return energy / 20.0 * 100;
    }
}

