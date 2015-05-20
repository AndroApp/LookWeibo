package com.shine.look.weibo.bean.gson;

import com.shine.look.weibo.bean.Comment;

import java.util.List;

/**
 * User:Shine
 * Date:2015-05-18
 * Description:
 */
public class CommentsInfo {
    public List<Comment> comments;
    public boolean hasvisible;
    public String previous_cursor;
    public String next_cursor;
    public int total_number;
}
