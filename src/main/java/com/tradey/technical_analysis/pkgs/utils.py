from datetime import datetime, timezone


def format_unix_ms_to_datetime(unix_ms: int) -> datetime:
    """Convert a Unix timestamp in milliseconds to a datetime."""
    return datetime.fromtimestamp(unix_ms / 1000).astimezone(timezone.utc)


def format_datetime_to_unix_ms(dt: datetime) -> int:
    """Convert a datetime object to Unix timestamp in milliseconds."""
    return int(dt.timestamp() * 1000)
