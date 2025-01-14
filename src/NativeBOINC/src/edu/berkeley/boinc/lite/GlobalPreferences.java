/* 
 * NativeBOINC - Native BOINC Client with Manager
 * Copyright (C) 2011, Mateusz Szpakowski
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */

package edu.berkeley.boinc.lite;

/**
 * @author mat
 *
 */
public class GlobalPreferences {
	public boolean run_on_batteries;
    // poorly named; what it really means is:
    // if false, suspend while on batteries
	public boolean run_if_user_active;
	public boolean run_gpu_if_user_active;
	public double idle_time_to_run;
	//public double suspend_if_no_recent_input;
	public double suspend_cpu_usage;
	public boolean leave_apps_in_memory;
	//public boolean confirm_before_connecting;
	//public boolean hangup_if_dialed;
	public boolean dont_verify_images;
	public double work_buf_min_days;
	public double work_buf_additional_days;
	public double max_ncpus_pct;
	//public int max_ncpus;
	public double cpu_scheduling_period_minutes;
	public double disk_interval;
	public double disk_max_used_gb;
	public double disk_max_used_pct;
	public double disk_min_free_gb;
	//public double vm_max_used_frac;
	public double ram_max_used_busy_frac;
	public double ram_max_used_idle_frac;
	public double max_bytes_sec_up;
	public double max_bytes_sec_down;
	public double cpu_usage_limit;
	public double daily_xfer_limit_mb;
	public int daily_xfer_period_days;
	public double run_if_battery_nl_than;
	public double run_if_temp_lt_than;
	public boolean run_always_when_plugged;
	public boolean xfer_only_when_wifi;
	
	public TimePreferences cpu_times = new TimePreferences();
	public TimePreferences net_times = new TimePreferences();
}
