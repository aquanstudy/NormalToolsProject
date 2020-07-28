package com.pillowcase.normal.tools.demo;

import android.annotation.SuppressLint;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import com.geek.thread.GeekThreadManager;
import com.geek.thread.ThreadPriority;
import com.geek.thread.ThreadType;
import com.geek.thread.task.GeekRunnable;
import com.pillowcase.logger.LoggerUtils;
import com.pillowcase.logger.impl.ILoggerOperation;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ILoggerOperation {
    private String[] data = new String[]{
            "1",
            "2",
            "3",
    };

    private LoggerUtils mLogger;

    private String result;

    @SuppressLint({"SetTextI18n", "WrongConstant"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mLogger = new LoggerUtils(true, "test");

//        log("onCreate", "result = " + maxProfit2(new int[]{7, 1, 5, 3, 6, 4}));
        final TextView infoTv = findViewById(R.id.info_tv);
        final AppCompatImageView imageView = findViewById(R.id.image);

        String data = "";
        PackageManager packageManager = getPackageManager(); // 获得PackageManager对象
        if (packageManager != null) {
            Intent intent = new Intent(Intent.ACTION_MAIN, null);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            List<ResolveInfo> resolveInfos = packageManager.queryIntentActivities(intent, 0);
            Collections.sort(resolveInfos, new ResolveInfo.DisplayNameComparator(packageManager));

            for (ResolveInfo reInfo : resolveInfos) {
                String label = (String) reInfo.loadLabel(packageManager); // 获得应用程序的Label
                String packageName = reInfo.activityInfo.packageName; // 获得应用程序的包名
                data += "\u3000-->App :  " + label + "\u2000 包名 ： " + packageName + "\n";

                try {
                    String permission = reInfo.activityInfo.permission;
                    data += "\u3000\u3000权限 :  " + permission + "\n";
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            infoTv.setText(data);
        }
        //Android 5.0+ 仅显示本应用的进程
//        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
//        if (manager != null && manager.getRunningAppProcesses() != null) {
//            for (ActivityManager.RunningAppProcessInfo info : manager.getRunningAppProcesses()) {
//                data += "\u3000-->进程名 : " + info.processName + "\n";
//            }
//            infoTv.setText(data);
//        }

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            UsageStatsManager usm = (UsageStatsManager) getSystemService("usagestats");
            Calendar calendar = Calendar.getInstance();
            long endTime = calendar.getTimeInMillis();
            calendar.add(Calendar.MINUTE, -1);
            long startTime = calendar.getTimeInMillis();
            List<UsageStats> usageStatsList = usm.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, startTime, endTime);
            for (UsageStats info : usageStatsList) {
                data += "\u3000-->进程名 : " + info.getPackageName() + "\n";
            }
            infoTv.setText(data);
        }

        GeekThreadManager.getInstance().execute(new GeekRunnable(ThreadPriority.BACKGROUND) {
            @Override
            public void run() {
                try {
                    log("run", "");
                    ProcessBuilder builder = new ProcessBuilder("/system/bin/top", "-n", "1");
                    InputStream in = null;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            result = infoTv.getText().toString();
                        }
                    });
                    String workdirectory = "/system/bin/";
                    //设置一个路径
                    if (workdirectory != null) {
                        builder.directory(new File(workdirectory));
                        builder.redirectErrorStream(true);
                        Process process = builder.start();
                        in = process.getInputStream();
                        byte[] re = new byte[1024];
                        while (in.read(re) != -1) {
                            result += new String(re) + "\n";
                            // System.out.println("result = "+new String(re,"UTF-8"));
                        }
                    }
                    if (in != null) {
                        in.close();
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            infoTv.setText(result);
                        }
                    });
                } catch (Exception e) {
                    error(e, "run");
                }
            }
        }, ThreadType.NORMAL_THREAD);

//        NetUtils netUtils = new NetUtils(this);
//        log("onCreate", "Net Connect : " + netUtils.isNetConnect());

//        EmulatorUtils emulatorUtils = new EmulatorUtils(this, new IEmulatorCheckListener() {
//
//            @Override
//            public void result(boolean isEmulator, String info) {
//                log("result", "是否是模拟器 : " + isEmulator + "\n" + info);
////                infoTv.setText("是否是模拟器 : " + isEmulator + "\n" + Utils.formatObject(info, 300) + "\n");
//            }
//        });
//
////        infoTv.setText(infoTv.getText() + "\n" + emulatorUtils.test(this));
//
//        AssetsUtils assetsUtils = new AssetsUtils(new IAssetsListener() {
//            @Override
//            public void TextFileResult(String data) {
//                log("TextFileResult", "Data : " + data);
//            }
//
//            @Override
//            public void ImageFileResult(Bitmap bitmap) {
//                imageView.setImageBitmap(bitmap);
//            }
//
//            @Override
//            public void VideoFileResult(AssetFileDescriptor fileDescriptor) {
//
//            }
//        });
//        assetsUtils.loadTextFile(this, "", "1.txt");
//        assetsUtils.loadImageFile(this, "", "tencent.png");
//        log("onCreate", "result = " + Arrays.toString(plusOne(new int[]{9})));
//        log("onCreate", "result = " + Arrays.toString(plusOne(new int[]{1, 3, 9})));
//        log("onCreate", "result = " + Arrays.toString(plusOne(new int[]{1, 9, 9})));
//        log("onCreate", "result = " + Arrays.toString(plusOne(new int[]{9, 8, 7, 6, 5, 4, 3, 2, 1, 0})));


//        log("onCreate", "result = " + removeDuplicates(new int[]{1, 1, 3}));
//        log("onCreate", "result = " + removeDuplicates(new int[]{1, 2, 3, 3, 4}));
//        log("onCreate", "result = " + removeDuplicates(new int[]{1, 2, 3, 3, 4, 5}));
//        log("onCreate", "result = " + removeDuplicates(new int[]{1, 2, 3, 3, 4, 4, 4, 5}));
//        log("onCreate", "result = " + removeDuplicates(new int[]{1, 2, 3, 3, 4, 4, 4, 5, 6}));
//        log("onCreate", "result = " + removeDuplicates(new int[]{0, 0, 1, 1, 1, 2, 2, 3, 3, 4}));

//        log("onCreate", "result = " + singleNumber(new int[]{2, 2, 1}));
//        log("onCreate", "result = " + singleNumber(new int[]{4, 1, 2, 1, 2}));
//        log("onCreate", "result = " + singleNumber(new int[]{3, 4, 5, 3, 1, 5, 6, 4, 1, 2, 1, 2}));

//        log("onCreate", "result = " + Arrays.toString(twoSum(new int[]{2, 7, 11, 15}, 9)));
//        log("onCreate", "result = " + Arrays.toString(twoSum(new int[]{1, 3, 2, 4, 5, 6, 7, 8, 9, 10, 11, 15}, 18)));

//        rotate(new int[]{1, 2, 3, 4, 5, 6, 7}, 3);
//        rotate(new int[]{1, 2}, 1);
//        moveZeroes(new int[]{1, 3, 2});
//        moveZeroes(new int[]{0, 0, 1});
//        moveZeroes(new int[]{0, 0});
    }

    public int maxProfit(int[] prices) {
        int data = 0;
        if (prices.length > 0) {
            int date = 0;
            while (date < prices.length) {
                if (date == prices.length - 1) {
                    break;
                }
                for (int i = date + 1; i < prices.length; i++) {
                    int num = prices[i] - prices[date];
                    if (num > data) {
                        data = num;
                    }
                }
                date++;
            }
        }
        return data;
    }

    public int maxProfit2(int[] prices) {
        int data = 0;
        if (prices.length > 0) {
            for (int i = 0; i < prices.length; i++) {
                if (i < prices.length - 1) {
                    if (prices[i] < prices[i + 1]) {
                        data += prices[i + 1] - prices[i];
                        log("maxProfit2", "num1 : " + prices[i] + " , num2 : " + prices[i + 1] + " , data : " + data);
                    }
                }
            }
        }
        return data;
    }

    public int[] plusOne(int[] digits) {
        if (digits.length > 0) {
            List<Integer> dataList = new ArrayList<>();
            for (int i : digits) {
                dataList.add(i);
            }
            int lastIndex = dataList.get(dataList.size() - 1) + 1;
            dataList.set(dataList.size() - 1, lastIndex);
            while (dataList.contains(10)) {
                for (int i = dataList.size() - 1; i >= 0; i--) {
                    if (dataList.get(i) == 10) {
                        dataList.set(i, 0);
                        if (i == 0) {
                            Collections.sort(dataList);
                            dataList.add(1);
                            Collections.reverse(dataList);
                        } else {
                            dataList.set(i - 1, dataList.get(i - 1) + 1);
                        }
                    }
                }
            }
            digits = new int[dataList.size()];
            for (int i = 0; i < dataList.size(); i++) {
                digits[i] = dataList.get(i);
            }
        }
        return digits;
    }

    public int removeDuplicates(int[] nums) {
        log("removeDuplicates", "nums : " + Arrays.toString(nums));
        int len = 0;
        if (nums != null) {
            for (int i = 0; i < nums.length; i++) {
                if (i == 0) {
                    len++;
                } else {
                    int lastNum = nums[i - 1];
//                    log("removeDuplicates", "i : " + i + " , currentNum : " + nums[i] + " , LastNum : " + lastNum);
                    if (nums[i] < lastNum) {
                        nums[i] = nums[i + 1];
//                        log("removeDuplicates", "CurrentNum < LastNum , nums : " + Arrays.toString(nums));
                    }

                    for (int j = i; j < nums.length; j++) {
                        int currentNum = nums[j];
//                        log("removeDuplicates", "j : " + j + " , currentNum : " + currentNum + " , LastNum : " + lastNum);
                        if (currentNum > lastNum) {
                            nums[i] = nums[j];
                            len++;
                            break;
                        } else if (j == nums.length - 1) {
                            nums[i] = nums[j];
//                            log("removeDuplicates", "j == nums.length, nums : " + Arrays.toString(nums));
                        }
                    }
                }
            }
        }
        log("removeDuplicates", "--> len : " + len + " , nums : " + Arrays.toString(nums));
        return len;
    }

    public boolean containsDuplicate(int[] nums) {
        boolean isFound = false;
        if (nums != null && nums.length > 1) {
            Arrays.sort(nums);
            for (int i = 0; i < nums.length; i++) {
                if (i + 1 <= nums.length - 1 && nums[i] == nums[i + 1]) {
                    isFound = true;
                    break;
                }
            }
        }

        return isFound;
    }

    public int singleNumber(int[] nums) {
        if (nums != null) {
            Arrays.sort(nums);
            log("singleNumber", "nums : " + Arrays.toString(nums));
            if (nums.length == 1) {
                return nums[0];
            }
            int last, current, next;
            for (int i = 0; i < nums.length; i++) {
                current = nums[i];
                log("singleNumber", "i : " + i + " , current : " + current);
                if (i == 0) {
                    next = nums[i + 1];
                    if (current == next) {
                        i++;
                    } else {
                        log("singleNumber", "Current != Next  -->current : " + current);
                        return current;
                    }
                } else {
                    last = nums[i - 1];
                    if (current == last) {
                        i++;
                    } else {
                        if (i + 1 < nums.length) {
                            next = nums[i + 1];
                            if (current == next) {
                                i++;
                            } else {
                                log("singleNumber", "Current != Next  -->current : " + current);
                                return current;
                            }
                        } else {
                            log("singleNumber", "Last Nums -->current : " + current);
                            return current;
                        }
                    }
                }
            }
        }
        return 0;
    }

    public int[] twoSum(int[] nums, int target) {
        int[] targetNums = null;

        for (int i = 0; i < nums.length; i++) {
            int num1 = nums[i];
            for (int j = i + 1; j < nums.length; j++) {
                int num2 = nums[j];
                if (num1 + num2 == target) {
                    targetNums = new int[]{i, j};
                }
            }
        }
        log("twoSum", "targetNums : " + Arrays.toString(targetNums));
        return targetNums;
    }

    public void rotate(int[] nums, int k) {
        log("rotate", "nums : " + Arrays.toString(nums));
        if (nums != null && k != 0) {
            for (int i = 0; i < k; i++) {
                int last = nums[nums.length - 1];
                for (int j = nums.length - 1; j > 0; j--) {
                    nums[j] = nums[j - 1];
                }
                nums[0] = last;
            }
        }
        log("rotate", "--> nums : " + Arrays.toString(nums));
    }

    public void moveZeroes(int[] nums) {
        log("moveZeroes", "nums : " + Arrays.toString(nums));
        if (nums != null && nums.length != 1) {
            int index = 0, count = nums.length - 1;
            while (index < count) {
                int current = nums[index];
                if (current == 0) {
                    for (int j = index; j < nums.length - 1; j++) {
                        log("moveZeroes", "change--> j : " + j + " , item : " + nums[j]);
                        nums[j] = nums[j + 1];
                    }
                    nums[nums.length - 1] = current;
                    log("moveZeroes", "index : " + index + " , change-->item : " + Arrays.toString(nums));
                    count--;
                } else {
                    index++;
                }
            }
        }
        log("moveZeroes", "--> nums : " + Arrays.toString(nums));
    }

    @Override
    public void log(String method, Object object) {
        mLogger.log(method, object);
    }

    @Override
    public void warn(String method, String message) {
        mLogger.warn(method, message);
    }

    @Override
    public void error(Throwable throwable, String method) {
        mLogger.error(throwable, method);
    }
}
