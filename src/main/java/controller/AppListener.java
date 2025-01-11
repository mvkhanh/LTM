package controller;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class AppListener implements ServletContextListener {
	private static ExecutorService executor;

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		// Tạo ThreadPoolExecutor khi ứng dụng được khởi động
		executor = new ThreadPoolExecutor(5, // Số luồng tối thiểu
				10, // Số luồng tối đa
				60L, TimeUnit.SECONDS, // Thời gian duy trì các luồng nhàn rỗi
				new ArrayBlockingQueue<>(100) // Hàng đợi chứa các yêu cầu
		);
		// Lưu executor vào ServletContext để có thể truy cập từ các servlet khác
		sce.getServletContext().setAttribute("executor", executor);
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		// Dọn dẹp khi ứng dụng dừng
		if (executor != null) {
			executor.shutdown();
		}
	}

	public static ExecutorService getExecutor() {
		return executor;
	}
}