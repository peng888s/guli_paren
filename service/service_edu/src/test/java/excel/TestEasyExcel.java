package excel;

import com.alibaba.excel.EasyExcel;

import java.util.ArrayList;
import java.util.List;

public class TestEasyExcel {
    public static void main(String[] args) {
        // 写操作
//        String fileName = "D:\\write.xlsx";
//        EasyExcel.write(fileName, ExcelDemo.class).sheet("学生列表").doWrite(getData());

        // 读操作
        String fileName = "D:\\write.xlsx";
        EasyExcel.read(fileName, ExcelDemo.class,new ExcelListener()).sheet().doRead();
    }

    public static List<ExcelDemo> getData(){
        List<ExcelDemo> list = new ArrayList<>();
        for (int i=0;i<10;i++){
            ExcelDemo excelDemo = new ExcelDemo();
            excelDemo.setSno(i);
            excelDemo.setSname("peng"+i);
            list.add(excelDemo);
        }
        return list;
    }
}
