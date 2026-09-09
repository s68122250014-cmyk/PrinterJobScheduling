import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Scanner;

// ==========================================
// Print Job
// ==========================================
class PrintJob {

    private String jobId;
    private String user;
    private int numberOfPages;
    private int submitTime;
    private int priority;
    private int sequence;

    public PrintJob(String jobId, String user, int numberOfPages,
                    int submitTime, int priority, int sequence) {

        this.jobId = jobId;
        this.user = user;
        this.numberOfPages = numberOfPages;
        this.submitTime = submitTime;
        this.priority = priority;
        this.sequence = sequence;
    }

    public String getJobId() {
        return jobId;
    }

    public String getUser() {
        return user;
    }

    public int getNumberOfPages() {
        return numberOfPages;
    }

    public int getSubmitTime() {
        return submitTime;
    }

    public int getPriority() {
        return priority;
    }

    public int getSequence() {
        return sequence;
    }

    @Override
    public String toString() {
        return String.format(
            "Job ID: %-5s | User: %-10s | Pages: %-3d | Submit: %-3d | Priority: %d",
            jobId,
            user,
            numberOfPages,
            submitTime,
            priority
        );
    }
}


// ==========================================
// Main Program
// ==========================================
public class PrinterJobScheduling {

    private static Scanner scanner = new Scanner(System.in);

    // Algorithm A : FIFO Queue
    private static Queue<PrintJob> fifoQueue = new ArrayDeque<>();

    // Algorithm B : Shortest Job First
    private static PriorityQueue<PrintJob> sjfQueue =
            new PriorityQueue<>(
                Comparator
                    .comparingInt(PrintJob::getNumberOfPages)
                    .thenComparingInt(PrintJob::getSequence)
            );

    private static int sequence = 0;
    private static int currentTime = 0;

    // ==========================================
    // Main
    // ==========================================
    public static void main(String[] args) {

        int algorithm;

        System.out.println("======================================");
        System.out.println("      PRINTER JOB SCHEDULING");
        System.out.println("======================================");

        System.out.println("เลือก Algorithm");
        System.out.println("1. Algorithm A : FIFO Queue");
        System.out.println("2. Algorithm B : Shortest Job First (SJF)");
        System.out.print("เลือก : ");

        algorithm = scanner.nextInt();
        scanner.nextLine();

        if (algorithm != 1 && algorithm != 2) {
            System.out.println("เลือก Algorithm ไม่ถูกต้อง");
            return;
        }

        System.out.println();

        while (true) {

            System.out.println("--------------------------------------");
            System.out.println("Algorithm: " +
                    (algorithm == 1 ? "FIFO" : "SJF"));
            System.out.println("Current Time: " + currentTime);
            System.out.println("--------------------------------------");

            System.out.println("1. SUBMIT");
            System.out.println("2. PRINT");
            System.out.println("3. CANCEL");
            System.out.println("4. PEEK");
            System.out.println("5. DISPLAY QUEUE");
            System.out.println("6. ADD SAMPLE JOBS");
            System.out.println("7. CALCULATE AVERAGE WAITING TIME");
            System.out.println("0. EXIT");

            System.out.print("Command : ");
            String command = scanner.nextLine().trim().toUpperCase();

            switch (command) {

                case "1":
                case "SUBMIT":
                    submitJob(algorithm);
                    break;

                case "2":
                case "PRINT":
                    printJob(algorithm);
                    break;

                case "3":
                case "CANCEL":
                    cancelJob(algorithm);
                    break;

                case "4":
                case "PEEK":
                    peekJob(algorithm);
                    break;

                case "5":
                case "DISPLAY QUEUE":
                case "DISPLAY":
                    displayQueue(algorithm);
                    break;

                case "6":
                case "ADD SAMPLE JOBS":
                    addSampleJobs(algorithm);
                    break;

                case "7":
                case "CALCULATE":
                    calculateAverageWaitingTime(algorithm);
                    break;

                case "0":
                case "EXIT":
                    System.out.println("จบการทำงาน");
                    return;

                default:
                    System.out.println("ไม่พบคำสั่งนี้");
            }
        }
    }


    // ==========================================
    // SUBMIT
    // ==========================================
    private static void submitJob(int algorithm) {

        System.out.println();
        System.out.println("========== SUBMIT JOB ==========");

        System.out.print("Job ID: ");
        String jobId = scanner.nextLine();

        System.out.print("User: ");
        String user = scanner.nextLine();

        System.out.print("Number of Pages: ");
        int pages = scanner.nextInt();

        System.out.print("Priority: ");
        int priority = scanner.nextInt();

        scanner.nextLine();

        PrintJob job = new PrintJob(
                jobId,
                user,
                pages,
                currentTime,
                priority,
                sequence++
        );

        if (algorithm == 1) {

            // Algorithm A
            fifoQueue.offer(job);

        } else {

            // Algorithm B
            sjfQueue.offer(job);
        }

        System.out.println("เพิ่ม Job สำเร็จ");
        System.out.println(job);
    }


    // ==========================================
    // PRINT
    // ==========================================
    private static void printJob(int algorithm) {

        PrintJob job = null;

        if (algorithm == 1) {

            // FIFO
            job = fifoQueue.poll();

        } else {

            // SJF
            job = sjfQueue.poll();
        }

        if (job == null) {
            System.out.println("Queue ว่าง ไม่มีงานให้พิมพ์");
            return;
        }

        int waitingTime = currentTime - job.getSubmitTime();

        System.out.println();
        System.out.println("========== PRINT ==========");
        System.out.println("กำลังพิมพ์ Job: " + job.getJobId());
        System.out.println("User: " + job.getUser());
        System.out.println("Pages: " + job.getNumberOfPages());
        System.out.println("Submit Time: " + job.getSubmitTime());
        System.out.println("Start Time: " + currentTime);
        System.out.println("Waiting Time: " + waitingTime);

        // สมมติ 1 หน้า = 1 หน่วยเวลา
        currentTime += job.getNumberOfPages();

        System.out.println("Finish Time: " + currentTime);
        System.out.println("พิมพ์เสร็จแล้ว");
    }


    // ==========================================
    // CANCEL
    // ==========================================
    private static void cancelJob(int algorithm) {

        System.out.println();
        System.out.println("========== CANCEL JOB ==========");

        System.out.print("Job ID ที่ต้องการยกเลิก: ");
        String jobId = scanner.nextLine();

        boolean removed = false;

        if (algorithm == 1) {

            // FIFO Queue
            PrintJob target = findJob(fifoQueue, jobId);

            if (target != null) {
                fifoQueue.remove(target);
                removed = true;
            }

        } else {

            // SJF PriorityQueue
            PrintJob target = findJob(sjfQueue, jobId);

            if (target != null) {
                sjfQueue.remove(target);
                removed = true;
            }
        }

        if (removed) {
            System.out.println("ยกเลิก Job " + jobId + " สำเร็จ");
        } else {
            System.out.println("ไม่พบ Job " + jobId);
        }
    }


    // ==========================================
    // FIND JOB
    // ==========================================
    private static PrintJob findJob(
            Queue<PrintJob> queue,
            String jobId) {

        for (PrintJob job : queue) {

            if (job.getJobId().equalsIgnoreCase(jobId)) {
                return job;
            }
        }

        return null;
    }


    // ==========================================
    // PEEK
    // ==========================================
    private static void peekJob(int algorithm) {

        PrintJob job;

        if (algorithm == 1) {
            job = fifoQueue.peek();
        } else {
            job = sjfQueue.peek();
        }

        System.out.println();
        System.out.println("========== PEEK ==========");

        if (job == null) {
            System.out.println("Queue ว่าง");
        } else {
            System.out.println("งานถัดไปคือ:");
            System.out.println(job);
        }
    }


    // ==========================================
    // DISPLAY QUEUE
    // ==========================================
    private static void displayQueue(int algorithm) {

        System.out.println();
        System.out.println("========== DISPLAY QUEUE ==========");

        if (algorithm == 1) {

            if (fifoQueue.isEmpty()) {
                System.out.println("Queue ว่าง");
                return;
            }

            int count = 1;

            for (PrintJob job : fifoQueue) {

                System.out.println(
                    count + ". " + job
                );

                count++;
            }

        } else {

            if (sjfQueue.isEmpty()) {
                System.out.println("Queue ว่าง");
                return;
            }

            // PriorityQueue ไม่รับประกันลำดับเมื่อวน for
            // จึง copy แล้วเรียงก่อนแสดงผล
            List<PrintJob> jobs =
                    new ArrayList<>(sjfQueue);

            jobs.sort(
                Comparator
                    .comparingInt(PrintJob::getNumberOfPages)
                    .thenComparingInt(PrintJob::getSequence)
            );

            int count = 1;

            for (PrintJob job : jobs) {

                System.out.println(
                    count + ". " + job
                );

                count++;
            }
        }
    }


    // ==========================================
    // ADD SAMPLE JOBS
    // ==========================================
    private static void addSampleJobs(int algorithm) {

        System.out.println();
        System.out.println("========== ADD SAMPLE JOBS ==========");

        PrintJob j1 = new PrintJob(
                "J1", "User1", 20, currentTime, 1, sequence++
        );

        PrintJob j2 = new PrintJob(
                "J2", "User2", 2, currentTime, 1, sequence++
        );

        PrintJob j3 = new PrintJob(
                "J3", "User3", 15, currentTime, 1, sequence++
        );

        PrintJob j4 = new PrintJob(
                "J4", "User4", 1, currentTime, 1, sequence++
        );

        PrintJob j5 = new PrintJob(
                "J5", "User5", 10, currentTime, 1, sequence++
        );

        if (algorithm == 1) {

            fifoQueue.offer(j1);
            fifoQueue.offer(j2);
            fifoQueue.offer(j3);
            fifoQueue.offer(j4);
            fifoQueue.offer(j5);

        } else {

            sjfQueue.offer(j1);
            sjfQueue.offer(j2);
            sjfQueue.offer(j3);
            sjfQueue.offer(j4);
            sjfQueue.offer(j5);
        }

        System.out.println("เพิ่มข้อมูลตัวอย่างสำเร็จ");
        System.out.println("J1 = 20 pages");
        System.out.println("J2 = 2 pages");
        System.out.println("J3 = 15 pages");
        System.out.println("J4 = 1 page");
        System.out.println("J5 = 10 pages");
    }


    // ==========================================
    // AVERAGE WAITING TIME
    // ==========================================
    private static void calculateAverageWaitingTime(int algorithm) {

        List<PrintJob> jobs = new ArrayList<>();

        if (algorithm == 1) {

            jobs.addAll(fifoQueue);

        } else {

            jobs.addAll(sjfQueue);

            jobs.sort(
                Comparator
                    .comparingInt(PrintJob::getNumberOfPages)
                    .thenComparingInt(PrintJob::getSequence)
            );
        }

        if (jobs.isEmpty()) {
            System.out.println("ไม่มี Job ใน Queue");
            return;
        }

        int time = currentTime;
        int totalWaitingTime = 0;

        System.out.println();
        System.out.println("========== WAITING TIME ==========");

        for (PrintJob job : jobs) {

            int waitingTime =
                    time - job.getSubmitTime();

            System.out.println(
                job.getJobId()
                + " -> Waiting Time = "
                + waitingTime
            );

            totalWaitingTime += waitingTime;

            time += job.getNumberOfPages();
        }

        double average =
                (double) totalWaitingTime / jobs.size();

        System.out.println("----------------------------------");
        System.out.println(
            "Average Waiting Time = "
            + average
        );
    }
}