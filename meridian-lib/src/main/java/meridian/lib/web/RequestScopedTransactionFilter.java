package meridian.lib.web;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import javax.servlet.*;
import javax.servlet.http.*;

import org.apache.commons.lang.StringUtils;
import org.slf4j.*;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.*;
import org.springframework.transaction.support.*;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import meridian.lib.util.WrappedServletException;

public class RequestScopedTransactionFilter extends OncePerRequestFilter {
	
	private static final Logger logger = LoggerFactory.getLogger(RequestScopedTransactionFilter.class);

	private PlatformTransactionManager transactionManager;
	private String transactionManagerRef = "transactionManager";
	
	public String getTransactionManagerRef() {
		return transactionManagerRef;
	}
	
	public void setTransactionManagerRef(String transactionManagerRef) {
		this.transactionManagerRef = transactionManagerRef;
	}
	
	@Override
	protected void initFilterBean() throws ServletException {
		super.initFilterBean();
		ApplicationContext appContext = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
		this.transactionManager = appContext.getBean(getTransactionManagerRef(), PlatformTransactionManager.class);
	}
	
	@Override
	public void doFilterInternal(final HttpServletRequest req, final HttpServletResponse resp, final FilterChain chain) throws IOException, ServletException {
		final Timer timer = new Timer();
		if(StringUtils.startsWith(req.getRequestURI(), "/admin/amq")){
			chain.doFilter(req, resp);
			return;
		}
		timer.start();
		TransactionTemplate tt = new TransactionTemplate(this.transactionManager);
		tt.setPropagationBehavior(TransactionTemplate.PROPAGATION_REQUIRED);
		tt.setReadOnly(false);
		timer.mark("Create TransactionTemplate");
		try {
			timer.start();
			tt.execute(new TransactionCallbackWithoutResult() {
				@Override
				protected void doInTransactionWithoutResult(TransactionStatus status) {
					try {
						long startTime = System.nanoTime();
						chain.doFilter(req, resp);
						timer.mark("chain.doFilter(req, resp);",startTime);
					} catch (IOException e) {
						throw new WrappedServletException(e);
					} catch (ServletException e) {
						throw new WrappedServletException(e);
					}
				}
			});
			timer.mark("Execute transaction");
		} catch (Exception e) {
			if (e instanceof WrappedServletException && e.getCause() != null) {
				e = (Exception) e.getCause();
			}
			logger.error("Unexpected error: " + e.getMessage(), e);
			if (e instanceof ServletException) {
				throw (ServletException) e;
			}
			if (e instanceof IOException) {
				throw (IOException) e;
			}
			if (e instanceof RuntimeException) {
				throw (RuntimeException) e;
			}
			throw new RuntimeException(e);
		}finally{
			timer.printTimes();
		}
	}
	private class Timer{
		private long startTime;
		private HashMap<String, Long> points = new LinkedHashMap<String, Long>();

		public void start(){
			this.startTime=System.nanoTime();
		}
		public void mark(String msg){
			mark(msg,this.startTime);
		}
		public void mark(String msg, long startTime){
			long time = System.nanoTime() - startTime;
			this.points.put(msg, time);
		}
		private long getTimeDiffInMilliSeconds(long time){
			return time / 1000000L;
		}
		public HashMap<String, Long> getPoints(){
			return this.points;
		}
		public void printTimes(){
			for(Entry<String, Long> entry : this.points.entrySet()){
				logger.info(entry.getKey() + " -> took:[{}]ms [{}]ns",getTimeDiffInMilliSeconds(entry.getValue()),entry.getValue());
			}
		}
	}
}
