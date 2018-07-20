package com.douglasrhx.awsLoadBalancer.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName("pools")
public class TargetGroup extends AbstractALBObject implements Serializable
{
	private static final long serialVersionUID = 4658507537282380278L;
}