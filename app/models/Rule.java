package models;

/**
 * Created with IntelliJ IDEA.
 * User: yuanzhe
 * Date: 13-11-28
 * Time: 下午7:42
 * To change this template use File | Settings | File Templates.
 */

/**
 * The class to describe inference rules, with a form "head <- body"
 */
public class Rule {
    private String rule;

    public Rule(String rule) {
        this.rule = rule;
    }

    public Rule(String head, String body) {
        this.rule = head+"<-"+body;
    }

    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }

}
