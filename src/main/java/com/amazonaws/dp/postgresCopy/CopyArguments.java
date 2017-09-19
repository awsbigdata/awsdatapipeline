package com.amazonaws.dp.postgresCopy;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

/**
 * Created by srramas on 9/13/17.
 */
@Parameters(separators = "=")
public class CopyArguments {

    @Parameter(names = "-jdbcurl", description = "Postgress JDBC URL", required = true)
    private String jdbcurl;


    @Parameter(names = "-copycommand", description = "copy command: example, COPY t FROM STDIN ", required = true)
    private String copy_command;

    @Parameter(names = "-input", description = "input file", required = true)
    private String inputfile;

    @Parameter(names = "--help", help = true)
    private boolean help = false;

   @Parameter(names = "-username", description = "postgress username", required = true)
    private  String username;

    @Parameter(names = "-password", description = "postgress  password", required = true)
    private String password;


    public String getJdbcurl() {
        return jdbcurl;
    }

    public String getCopy_command() {
        return copy_command;
    }

    public String getInputfile() {
        return inputfile;
    }

    public boolean isHelp() {
        return help;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setJdbcurl(String jdbcurl) {
        this.jdbcurl = jdbcurl;
    }

    public void setCopy_command(String copy_command) {
        this.copy_command = copy_command;
    }

    public void setInputfile(String inputfile) {
        this.inputfile = inputfile;
    }

    public void setHelp(boolean help) {
        this.help = help;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "CopyArguments{" +
                "jdbcurl='" + jdbcurl + '\'' +
                ", copy_command='" + copy_command + '\'' +
                ", inputfile='" + inputfile + '\'' +
                ", help=" + help +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
