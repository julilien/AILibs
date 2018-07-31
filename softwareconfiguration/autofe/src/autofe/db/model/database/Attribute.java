package autofe.db.model.database;

public class Attribute implements Comparable<Attribute> {

	private String name;

	private AttributeType type;

	private boolean isTarget;

	private boolean isPrimaryKey;

	public Attribute(String name, AttributeType type) {
		super();
		this.name = name;
		this.type = type;
		this.isTarget = false;
		this.isPrimaryKey = false;
	}

	public Attribute(String name, AttributeType type, boolean isTarget) {
		super();
		this.name = name;
		this.type = type;
		this.isTarget = isTarget;
		this.isPrimaryKey = false;
	}

	public Attribute(String name, AttributeType type, boolean isTarget, boolean isPrimaryKey) {
		super();
		this.name = name;
		this.type = type;
		this.isTarget = isTarget;
		this.isPrimaryKey = isPrimaryKey;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public AttributeType getType() {
		return type;
	}

	public void setType(AttributeType type) {
		this.type = type;
	}

	public boolean isTarget() {
		return isTarget;
	}

	public void setTarget(boolean isTarget) {
		this.isTarget = isTarget;
	}

	public boolean isAggregable() {
		return this.type.isAggregable();
	}

	public boolean isFeature() {
		return !(this.type == AttributeType.ID);
	}

	public boolean isPrimaryKey() {
		return isPrimaryKey;
	}

	public void setPrimaryKey(boolean isPrimaryKey) {
		this.isPrimaryKey = isPrimaryKey;
	}

	@Override
	public int compareTo(Attribute o) {
		return name.compareTo(o.name);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (isPrimaryKey ? 1231 : 1237);
		result = prime * result + (isTarget ? 1231 : 1237);
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Attribute other = (Attribute) obj;
		if (isPrimaryKey != other.isPrimaryKey)
			return false;
		if (isTarget != other.isTarget)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (type != other.type)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Attribute [name=" + name + ", type=" + type + ", isTarget=" + isTarget + ", isPrimaryKey="
				+ isPrimaryKey + "]";
	}

}
